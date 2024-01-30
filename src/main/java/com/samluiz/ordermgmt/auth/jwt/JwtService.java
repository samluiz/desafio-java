package com.samluiz.ordermgmt.auth.jwt;

import com.samluiz.ordermgmt.auth.user.models.User;
import com.samluiz.ordermgmt.auth.user.services.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${auth.jwt.secret-key}")
    private String SECRET_KEY;

    private final UserService userService;

    public JwtService(UserService userService) {
        this.userService = userService;
    }

    public String extractUserAuth(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Map<String, String> generateTokens(User userDetails) {
        return generateTokens(new HashMap<>(), userDetails);
    }

    public Map<String, String> generateTokens(
            Map<String, Object> extraClaims,
            User userDetails
    ) {

        long nowMillis = System.currentTimeMillis();
        long accessTokenExpMillis = nowMillis + (20 * 60 * 1000);
        long refreshTokenExpMillis = nowMillis + (7 * 24 * 60 * 60 * 1000);
        Date accessTokenExp = new Date(accessTokenExpMillis);
        Date refreshTokenExp = new Date(refreshTokenExpMillis);

        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("refresh", true);

        String accessToken = Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(accessTokenExp)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(refreshClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(refreshTokenExp)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return tokens;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userAuth = extractUserAuth(token);
        return (userAuth.equals(userDetails.getUsername())) && !isTokenExpired(token) && !isRefreshToken(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private boolean isRefreshToken(String token) {
        return extractAllClaims(token).containsKey("refresh");
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Map<String, String> refreshTokens(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(refreshToken);

            String username = claims.getBody().getSubject();
            User user = userService.findByUsername(username);

            Date now = new Date();
            Date refreshTokenExp = claims.getBody().getExpiration();
            if (refreshTokenExp.before(now)) {
                throw new ExpiredJwtException(null, null, "Expired refresh token");
            }

            return generateTokens(user);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Invalid refresh token", e);
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
