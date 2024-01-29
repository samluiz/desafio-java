package com.samluiz.ordermgmt.pedido.services;

import com.samluiz.ordermgmt.common.exceptions.ResourceNotFoundException;
import com.samluiz.ordermgmt.pedido.models.Pedido;
import com.samluiz.ordermgmt.pedido.repositories.PedidoRepository;
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

    public Pedido findById(UUID id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Page<Pedido> findAll(Pageable pageable) {
        return pedidoRepository.findAll(pageable);
    }

    public Pedido create(Pedido obj) {
        return pedidoRepository.save(obj);
    }
}
