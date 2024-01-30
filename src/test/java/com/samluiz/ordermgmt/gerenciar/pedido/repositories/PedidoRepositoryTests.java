package com.samluiz.ordermgmt.gerenciar.pedido.repositories;

import com.samluiz.ordermgmt.gerenciar.pedido.models.Pedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class PedidoRepositoryTests {

    @Autowired
    private PedidoRepository pedidoRepository;

    private UUID existingPedidoId;
    private UUID nonExistingPedidoId;

    @Test
    void findById_ExistingId_ReturnsOptionalOfPedido() {
        Pedido existingPedido = new Pedido();
        existingPedidoId = pedidoRepository.save(existingPedido).getId();

        Optional<Pedido> result = pedidoRepository.findById(existingPedidoId);

        assertTrue(result.isPresent());
        assertEquals(existingPedido, result.get());
    }

    @Test
    void findById_NonExistingId_ReturnsEmptyOptional() {
        nonExistingPedidoId = UUID.randomUUID();

        Optional<Pedido> result = pedidoRepository.findById(nonExistingPedidoId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_NoException_ReturnsPageOfPedidos() {
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        pedidoRepository.saveAll(List.of(pedido1, pedido2));

        Page<Pedido> result = pedidoRepository.findAll(PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void save_NoException_ReturnsSavedPedido() {
        Pedido pedidoToSave = new Pedido();

        Pedido result = pedidoRepository.save(pedidoToSave);

        assertNotNull(result.getId());
    }

    @Test
    @Transactional
    void saveWithList_NoException_ReturnsSavedPedidoList() {
        List<Pedido> pedidosToSave = List.of(new Pedido(), new Pedido());

        List<Pedido> result = pedidoRepository.saveAll(pedidosToSave);

        assertEquals(2, result.size());
        assertNotNull(result.get(0).getId());
    }
}