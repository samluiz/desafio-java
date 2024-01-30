package com.samluiz.ordermgmt.gerenciar.pedido.repositories;

import com.samluiz.ordermgmt.gerenciar.pedido.models.ItemPedido;
import com.samluiz.ordermgmt.gerenciar.pedido.models.Pedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class ItemPedidoRepositoryTests {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    private UUID existingItemPedidoId;
    private UUID nonExistingItemPedidoId;

    @Test
    void findById_ExistingId_ReturnsOptionalOfItemPedido() {
        ItemPedido existingItemPedido = new ItemPedido();
         existingItemPedidoId = itemPedidoRepository.save(existingItemPedido).getId();

        Optional<ItemPedido> result = itemPedidoRepository.findById(existingItemPedidoId);

        assertTrue(result.isPresent());
        assertEquals(existingItemPedido, result.get());
    }

    @Test
    void findById_NonExistingId_ReturnsEmptyOptional() {
        nonExistingItemPedidoId = UUID.randomUUID();

        Optional<ItemPedido> result = itemPedidoRepository.findById(nonExistingItemPedidoId);

        assertTrue(result.isEmpty());
    }

    @Test
    void save_NoException_ReturnsSavedItemPedido() {
        ItemPedido itemToSave = new ItemPedido();

        ItemPedido result = itemPedidoRepository.save(itemToSave);

        assertNotNull(result.getId());
    }
}
