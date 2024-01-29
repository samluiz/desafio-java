package com.samluiz.ordermgmt.pedido.services;

import com.samluiz.ordermgmt.common.exceptions.PedidoException;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.pedido.models.Pedido;
import com.samluiz.ordermgmt.pedido.repositories.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    public Pedido findById(UUID id) {
        try {
            return pedidoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(id));
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar pedido com id {} -> Erro: {}", id, e.getMessage());
            throw new PedidoException("Erro ao buscar pedido com id " + id);
        }
    }

    public Page<Pedido> findAll(Pageable pageable) {
        try {
            return pedidoRepository.findAll(pageable);
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar pedidos -> Erro: {}", e.getMessage());
            throw new PedidoException("Erro ao buscar pedidos.");
        }
    }

    public Pedido create(Pedido obj) {
        try {
            return pedidoRepository.save(obj);
        } catch (DataAccessException e) {
            logger.error("Erro ao criar pedido -> Erro: {}", e.getMessage());
            throw new PedidoException("Erro ao criar pedido.");
        }
    }
}
