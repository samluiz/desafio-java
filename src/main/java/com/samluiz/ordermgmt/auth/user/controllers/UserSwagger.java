package com.samluiz.ordermgmt.auth.user.controllers;

import com.samluiz.ordermgmt.auth.user.dtos.CreateUserDTO;
import com.samluiz.ordermgmt.auth.user.dtos.UserDTO;
import com.samluiz.ordermgmt.auth.user.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Usuários")
@RequestMapping("/usuarios")
public interface UserSwagger {

    @Operation(summary = "Retorna o usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Usuário autenticado", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") })
    @PermitAll
    @GetMapping("/me")
    ResponseEntity<UserDTO> me(@AuthenticationPrincipal User user);

    @Operation(summary = "Busca um usuário pelo ID")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/busca/id/{id}")
    ResponseEntity<UserDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Busca um usuário pelo username")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/busca/username/{username}")
    ResponseEntity<UserDTO> findByUsername(@PathVariable String username);

    @Operation(summary = "Cria um usuário")
    @ApiResponse(responseCode = "201", description = "Usuário criado", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    ResponseEntity<UserDTO> create(@RequestBody @Valid CreateUserDTO user);
}
