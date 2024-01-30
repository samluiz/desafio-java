package com.samluiz.ordermgmt.gerenciar.pedido.controllers;

import com.samluiz.ordermgmt.common.utils.ControllerUtils;
import com.samluiz.ordermgmt.gerenciar.pedido.dtos.CriarOuAdicionarPedidoDTO;
import com.samluiz.ordermgmt.gerenciar.pedido.models.Pedido;
import com.samluiz.ordermgmt.gerenciar.pedido.services.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
public class PedidoController implements PedidoSwagger {

    private final PedidoService pedidoService;
    private final ControllerUtils<Pedido> utils;

    public PedidoController(PedidoService pedidoService, ControllerUtils<Pedido> utils) {
        this.pedidoService = pedidoService;
        this.utils = utils;
    }

    @Override
    public ResponseEntity<Pedido> findById(UUID id) {
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        return ResponseEntity.ok().body(pedido);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findAll(Integer page, Integer size) {
        PageRequest pagination = PageRequest.of(page, size);
        Page<Pedido> pedidos = pedidoService.buscarTodos(pagination);
        Map<String, Object> pedidosResponse = utils.generateResponse(pedidos);
        return ResponseEntity.ok().body(pedidosResponse);
    }

    @Override
    public ResponseEntity<Pedido> create(CriarOuAdicionarPedidoDTO dto) {
        Pedido pedidoCriado = pedidoService.criarPedido(dto);
        URI pedidoCriadoUri = utils.generateURI(pedidoCriado.getId());
        return ResponseEntity.created(pedidoCriadoUri).body(pedidoCriado);
    }

    @Override
    public ResponseEntity<Pedido> addItem(UUID id, CriarOuAdicionarPedidoDTO dto) {
        Pedido pedidoAtualizado = pedidoService.adicionarNovoItem(id, dto);
        return ResponseEntity.ok().body(pedidoAtualizado);
    }

    @Override
    public ResponseEntity<Pedido> removeItem(UUID pedidoId, UUID itemId) {
        Pedido pedidoAtualizado = pedidoService.removerItem(pedidoId, itemId);
        return ResponseEntity.ok().body(pedidoAtualizado);
    }

    @Override
    public ResponseEntity<Pedido> adicionarQuantidade(UUID itemId, Integer quantidade) {
        Pedido pedidoAtualizado = pedidoService.aumentarQuantidadeProdutoItem(itemId, quantidade);
        return ResponseEntity.ok().body(pedidoAtualizado);
    }

    @Override
    public ResponseEntity<Pedido> diminuirQuantidade(UUID itemId, Integer quantidade) {
        Pedido pedidoAtualizado = pedidoService.diminuirQuantidadeProdutoItem(itemId, quantidade);
        return ResponseEntity.ok().body(pedidoAtualizado);
    }
}
