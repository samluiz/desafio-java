package com.samluiz.ordermgmt.auth.controllers;

import com.samluiz.ordermgmt.auth.dtos.AuthDTO;
import com.samluiz.ordermgmt.auth.dtos.AuthRefreshDTO;
import com.samluiz.ordermgmt.auth.dtos.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Autenticação")
@RequestMapping("/auth")
public interface AuthSwagger {

    @Operation(summary = "Autentica um usuário")
    @ApiResponse(responseCode = "200", description = "Usuário autenticado", content = { @Content(schema = @Schema(implementation = AuthResponseDTO.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "400", description = "Usuário inválido")
    @ApiResponse(responseCode = "401", description = "Usuário não autorizado")
    @PostMapping("/login")
    ResponseEntity<AuthResponseDTO> login(@RequestBody AuthDTO dto);

    @Operation(summary = "Atualiza o token de acesso")
    @ApiResponse(responseCode = "200", description = "Token atualizado", content = { @Content(schema = @Schema(implementation = AuthResponseDTO.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "400", description = "Token inválido")
    @ApiResponse(responseCode = "401", description = "Token não autorizado")
    @PostMapping("/refresh")
    ResponseEntity<AuthResponseDTO> refresh(@RequestBody AuthRefreshDTO dto, HttpServletRequest request, HttpServletResponse response);

}
