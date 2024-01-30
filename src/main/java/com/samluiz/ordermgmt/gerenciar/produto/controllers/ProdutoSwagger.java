package com.samluiz.ordermgmt.gerenciar.produto.controllers;

import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Produto")
@RequestMapping("/produtos")
public interface ProdutoSwagger {

    @Operation(summary = "Busca um produto pelo ID")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @PreAuthorize("hasRole('ROLE_VIEWER')")
    @GetMapping("/{id}")
    ResponseEntity<Produto> findById(@PathVariable UUID id);

    @Operation(summary = "Busca todos os produtos")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    @PreAuthorize("hasRole('ROLE_VIEWER')")
    @GetMapping
    ResponseEntity<Map<String, Object>> findAll(@RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer size);

    @Operation(summary = "Cria um produto")
    @ApiResponse(responseCode = "201", description = "Produto criado")
    @ApiResponse(responseCode = "400", description = "Produto inválido")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PostMapping
    ResponseEntity<Produto> create(@RequestBody @Valid Produto produto);

    @Operation(summary = "Atualiza um produto")
    @ApiResponse(responseCode = "200", description = "Produto atualizado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    @PutMapping("/{id}")
    ResponseEntity<Produto> update(@RequestBody @Valid Produto produto, @PathVariable UUID id);

    @Operation(summary = "Deleta um produto")
    @ApiResponse(responseCode = "204", description = "Produto deletado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id);

}
