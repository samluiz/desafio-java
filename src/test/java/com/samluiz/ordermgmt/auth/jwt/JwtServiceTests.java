package com.samluiz.ordermgmt.auth.jwt;

import com.samluiz.ordermgmt.auth.user.models.User;
import com.samluiz.ordermgmt.auth.user.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class JwtServiceTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private JwtService jwtService;

    @Value("${auth.jwt.secret-key}")
    private String secretKey;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", secretKey);
    }

    @Test
    void generateTokens_ValidUser_ReturnsMapWithTokens() {
        User user = new User();
        user.setUsername("testUser");

        Map<String, String> tokens = jwtService.generateTokens(user);

        assertNotNull(tokens);
        assertTrue(tokens.containsKey("access_token"));
        assertTrue(tokens.containsKey("refresh_token"));
        assertFalse(tokens.get("access_token").isEmpty());
        assertFalse(tokens.get("refresh_token").isEmpty());
    }

    @Test
    void isTokenValid_ValidTokenAndUserDetails_ReturnsTrue() {
        User user = new User();
        user.setUsername("testUser");

        String token = generateToken(user.getUsername(), 20);

        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_ExpiredToken_ThrowsSignatureException() {
        User user = new User();
        user.setUsername("testUser");

        String expiredToken = generateToken(user.getUsername(), -1);

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(expiredToken, user));
    }

    @Test
    void isTokenValid_InvalidUser_ThrowsInvalidSignature() {
        User user = new User();
        user.setUsername("testUser");

        String token = generateToken("differentUser", 20);

        assertFalse(jwtService.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_RefreshToken_ReturnsFalse() {
        User user = new User();
        user.setUsername("testUser");

        String refreshToken = jwtService.generateTokens(new HashMap<>(), user).get("refresh_token");

        assertFalse(jwtService.isTokenValid(refreshToken, user));
    }

    @Test
    void refreshTokens_ValidRefreshToken_ReturnsNewTokens() {
        User user = new User();
        user.setUsername("testUser");

        String refreshToken = jwtService.generateTokens(new HashMap<>(), user).get("refresh_token");

        when(userService.findByUsername(anyString())).thenReturn(user);

        Map<String, String> newTokens = jwtService.refreshTokens(refreshToken);

        assertNotNull(newTokens);
        assertTrue(newTokens.containsKey("access_token"));
        assertTrue(newTokens.containsKey("refresh_token"));
        assertFalse(newTokens.get("access_token").isEmpty());
        assertFalse(newTokens.get("refresh_token").isEmpty());
    }

    @Test
    void refreshTokens_ExpiredRefreshToken_ThrowsExpiredJwtException() {
        String expiredToken = generateRefreshToken("testUser", -1);

        assertThrows(JwtException.class, () -> jwtService.refreshTokens(expiredToken));
    }

    @Test
    void refreshTokens_InvalidRefreshToken_ThrowsJwtException() {
        String invalidToken = "invalidToken";

        assertThrows(JwtException.class, () -> jwtService.refreshTokens(invalidToken));
    }

    private String generateToken(String username, int expiresInMinutes) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + (expiresInMinutes * 60 * 1000);
        Date expiration = new Date(expMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(expiration)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(String username, int expiresInMinutes) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + (expiresInMinutes * 60 * 1000);
        Date expiration = new Date(expMillis);

        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("refresh", true);

        return Jwts.builder()
                .setClaims(refreshClaims)
                .setSubject(username)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(expiration)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}