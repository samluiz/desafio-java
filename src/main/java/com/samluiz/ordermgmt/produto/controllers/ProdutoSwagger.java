package com.samluiz.ordermgmt.produto.controllers;

import com.samluiz.ordermgmt.produto.models.Produto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Produto")
@RequestMapping("/produtos")
public interface ProdutoSwagger {

    @Operation(summary = "Busca um produto pelo ID")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @GetMapping("/{id}")
    ResponseEntity<Produto> findById(UUID id);

    @Operation(summary = "Busca todos os produtos")
    @ApiResponse(responseCode = "200", description = "Produtos encontrados")
    @GetMapping
    ResponseEntity<Map<String, Object>> findAll(@RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "10") Integer size);

    @Operation(summary = "Cria um produto")
    @ApiResponse(responseCode = "201", description = "Produto criado")
    @ApiResponse(responseCode = "400", description = "Produto inválido")
    @PostMapping
    ResponseEntity<Produto> create(Produto produto);

    @Operation(summary = "Atualiza um produto")
    @ApiResponse(responseCode = "200", description = "Produto atualizado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @PutMapping("/{id}")
    ResponseEntity<Produto> update(Produto produto, UUID id);

    @Operation(summary = "Deleta um produto")
    @ApiResponse(responseCode = "204", description = "Produto deletado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(UUID id);

}
