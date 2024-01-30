package com.samluiz.ordermgmt.auth.services;

import com.samluiz.ordermgmt.auth.dtos.AuthDTO;
import com.samluiz.ordermgmt.auth.dtos.AuthResponseDTO;
import com.samluiz.ordermgmt.auth.jwt.JwtService;
import com.samluiz.ordermgmt.auth.user.models.User;
import com.samluiz.ordermgmt.auth.user.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AuthServiceTests {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticate_ValidAuthDTO_ReturnsAuthResponseDTO() {
        AuthDTO authDTO = AuthDTO.create("testUser", "testPassword");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        User user = new User();
        user.setUsername("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", "testAccessToken");
        tokens.put("refresh_token", "testRefreshToken");
        when(jwtService.generateTokens(user)).thenReturn(tokens);

        AuthResponseDTO authResponseDTO = authService.authenticate(authDTO);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, times(1)).findByUsername("testUser");
        verify(jwtService, times(1)).generateTokens(user);

        verifyNoMoreInteractions(authenticationManager, userService, jwtService);
    }

    @Test
    void refreshToken_ValidRefreshToken_ReturnsAuthResponseDTO() {
        String refreshToken = "testRefreshToken";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", "testUpdatedAccessToken");
        tokens.put("refresh_token", "testUpdatedRefreshToken");
        when(jwtService.refreshTokens(refreshToken)).thenReturn(tokens);

        AuthResponseDTO authResponseDTO = authService.refreshToken(refreshToken, request, response);

        verify(jwtService, times(1)).refreshTokens(refreshToken);

        verifyNoMoreInteractions(jwtService);
    }
}