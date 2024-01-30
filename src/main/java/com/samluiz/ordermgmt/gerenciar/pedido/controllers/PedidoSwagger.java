package com.samluiz.ordermgmt.gerenciar.pedido.controllers;

import com.samluiz.ordermgmt.gerenciar.pedido.dtos.CriarPedidoDTO;
import com.samluiz.ordermgmt.gerenciar.pedido.models.Pedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Pedido")
@RequestMapping("/pedidos")
public interface PedidoSwagger {

    @Operation(summary = "Busca um pedido pelo ID")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @PreAuthorize("hasRole('ROLE_VIEWER')")
    @GetMapping("/{id}")
    ResponseEntity<Pedido> findById(@PathVariable UUID id);

    @Operation(summary = "Busca todos os pedidos")
    @ApiResponse(responseCode = "200", description = "Pedidos encontrados")
    @PreAuthorize("hasRole('ROLE_VIEWER')")
    @GetMapping
    ResponseEntity<Map<String, Object>> findAll(@RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer size);

    @Operation(summary = "Cria um pedido")
    @ApiResponse(responseCode = "201", description = "Pedido criado")
    @ApiResponse(responseCode = "400", description = "Pedido inválido")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PostMapping
    ResponseEntity<Pedido> create(@RequestBody @Valid CriarPedidoDTO dto);
}
