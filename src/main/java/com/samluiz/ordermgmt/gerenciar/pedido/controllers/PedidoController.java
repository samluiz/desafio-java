package com.samluiz.ordermgmt.gerenciar.pedido.controllers;

import com.samluiz.ordermgmt.common.utils.ControllerUtils;
import com.samluiz.ordermgmt.gerenciar.pedido.dtos.CriarPedidoDTO;
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
        Pedido pedido = pedidoService.findById(id);
        return ResponseEntity.ok().body(pedido);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findAll(Integer page, Integer size) {
        PageRequest pagination = PageRequest.of(page, size);
        Page<Pedido> pedidos = pedidoService.findAll(pagination);
        Map<String, Object> pedidosResponse = utils.generateResponse(pedidos);
        return ResponseEntity.ok().body(pedidosResponse);
    }

    @Override
    public ResponseEntity<Pedido> create(CriarPedidoDTO dto) {
        Pedido pedidoCriado = pedidoService.create(dto);
        URI pedidoCriadoUri = utils.generateURI(pedidoCriado.getId());
        return ResponseEntity.created(pedidoCriadoUri).body(pedidoCriado);
    }
}
