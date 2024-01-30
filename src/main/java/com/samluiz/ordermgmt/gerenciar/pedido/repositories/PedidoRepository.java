package com.samluiz.ordermgmt.gerenciar.pedido.repositories;

import com.samluiz.ordermgmt.gerenciar.pedido.models.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    Page<Pedido> findAll(Pageable pageable);
}
