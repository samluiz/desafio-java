package com.samluiz.ordermgmt.auth.services;

import com.samluiz.ordermgmt.auth.dtos.AuthDTO;
import com.samluiz.ordermgmt.auth.dtos.AuthResponseDTO;
import com.samluiz.ordermgmt.auth.jwt.JwtService;
import com.samluiz.ordermgmt.auth.user.services.UserService;
import com.samluiz.ordermgmt.auth.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private static final String ACEESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    public AuthResponseDTO authenticate(AuthDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userService.findByUsername(dto.getUsername());
        var tokens = jwtService.generateTokens(user);
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(tokens.get(ACEESS_TOKEN));
        response.setRefreshToken(tokens.get(REFRESH_TOKEN));
        return response;
    }

    public AuthResponseDTO refreshToken(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> tokens = new HashMap<>();
        try {
            tokens = jwtService.refreshTokens(refreshToken);
            AuthResponseDTO authResponse = new AuthResponseDTO();
            authResponse.setToken(tokens.get(ACEESS_TOKEN));
            authResponse.setRefreshToken(tokens.get(REFRESH_TOKEN));
            return authResponse;
        } catch (Exception e) {
            SecurityUtils.sendErrorResponse(response, request, e);
        }
        return null;
    }
}
