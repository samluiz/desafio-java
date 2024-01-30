package com.samluiz.ordermgmt.auth.controllers;

import com.samluiz.ordermgmt.auth.dtos.AuthDTO;
import com.samluiz.ordermgmt.auth.dtos.AuthRefreshDTO;
import com.samluiz.ordermgmt.auth.dtos.AuthResponseDTO;
import com.samluiz.ordermgmt.auth.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthSwagger {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<AuthResponseDTO> login(AuthDTO dto) {
        AuthResponseDTO response = authService.authenticate(dto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthResponseDTO> refresh(AuthRefreshDTO dto, HttpServletRequest request, HttpServletResponse response) {
        AuthResponseDTO authResponse = authService.refreshToken(dto.getRefreshToken(), request, response);
        return ResponseEntity.ok(authResponse);
    }
}
