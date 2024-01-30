package com.samluiz.ordermgmt.auth.user.controllers;

import com.samluiz.ordermgmt.auth.user.dtos.UserDTO;
import com.samluiz.ordermgmt.auth.user.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Usuários")
@RequestMapping("/usuarios")
public interface UserSwagger {

    @Operation(summary = "Busca um usuário pelo ID")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/busca/id/{id}")
    ResponseEntity<UserDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Busca um usuário pelo username")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") })
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/busca/username/{username}")
    ResponseEntity<UserDTO> findByUsername(@PathVariable String username);

    @Operation(summary = "Cria um usuário")
    @ApiResponse(responseCode = "201", description = "Usuário criado", content = { @Content(schema = @Schema(implementation = UserDTO.class), mediaType = "application/json") })
    @PostMapping
    ResponseEntity<UserDTO> create(@RequestBody User user);
}
