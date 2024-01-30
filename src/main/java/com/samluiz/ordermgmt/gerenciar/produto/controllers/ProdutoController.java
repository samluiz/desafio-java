package com.samluiz.ordermgmt.gerenciar.produto.controllers;

import com.samluiz.ordermgmt.common.utils.ControllerUtils;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
import com.samluiz.ordermgmt.gerenciar.produto.services.ProdutoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
public class ProdutoController implements ProdutoSwagger {
    private final ProdutoService produtoService;
    private final ControllerUtils<Produto> utils;

    public ProdutoController(ProdutoService produtoService, ControllerUtils<Produto> utils) {
        this.produtoService = produtoService;
        this.utils = utils;
    }

    @Override
    public ResponseEntity<Produto> findById(UUID id) {
        Produto produto = produtoService.findById(id);
        return ResponseEntity.ok().body(produto);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findAll(Integer page, Integer size) {
        PageRequest pagination = PageRequest.of(page, size);
        Page<Produto> produtos = produtoService.findAll(pagination);
        Map<String, Object> produtosResponse = utils.generateResponse(produtos);
        return ResponseEntity.ok().body(produtosResponse);
    }

    @Override
    public ResponseEntity<Produto> create(Produto produto) {
        Produto produtoCriado = produtoService.create(produto);
        URI produtoCriadoUri = utils.generateURI(produtoCriado.getId());
        return ResponseEntity.created(produtoCriadoUri).body(produtoCriado);
    }

    @Override
    public ResponseEntity<Produto> update(Produto produto, UUID id) {
        Produto produtoAtualizado = produtoService.update(produto, id);
        return ResponseEntity.ok().body(produtoAtualizado);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
