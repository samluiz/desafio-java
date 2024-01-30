package com.samluiz.ordermgmt.gerenciar.pedido.services;

import com.samluiz.ordermgmt.common.exceptions.PedidoException;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.gerenciar.pedido.dtos.ItemPedidoDTO;
import com.samluiz.ordermgmt.gerenciar.pedido.models.ItemPedido;
import com.samluiz.ordermgmt.gerenciar.pedido.repositories.ItemPedidoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static com.samluiz.ordermgmt.utils.ControllerTestUtils.montarItemPedidoDTO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ItemPedidoServiceTests {

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private ItemPedidoService itemPedidoService;

    @Test
    void findById_ExistingId_ReturnsItemPedido() {
        UUID existingItemPedidoId = UUID.randomUUID();
        ItemPedido existingItemPedido = new ItemPedido();
        existingItemPedido.setId(existingItemPedidoId);

        when(itemPedidoRepository.findById(existingItemPedidoId)).thenReturn(Optional.of(existingItemPedido));

        ItemPedido result = itemPedidoService.buscarItemPorId(existingItemPedidoId);

        assertEquals(existingItemPedidoId, result.getId());
    }

    @Test
    void findById_NonExistingId_ThrowsRecursoNaoEncontradoException() {
        UUID nonExistingItemPedidoId = UUID.randomUUID();

        when(itemPedidoRepository.findById(nonExistingItemPedidoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> itemPedidoService.buscarItemPorId(nonExistingItemPedidoId));
    }

    @Test
    void saveItem_ValidItemPedido_ReturnsItemPedido() {
        UUID existingItemPedidoId = UUID.randomUUID();
        ItemPedido existingItemPedido = new ItemPedido();
        existingItemPedido.setId(existingItemPedidoId);

        when(itemPedidoRepository.save(existingItemPedido)).thenReturn(existingItemPedido);

        ItemPedido result = itemPedidoService.salvarItem(existingItemPedido);

        assertEquals(existingItemPedidoId, result.getId());
    }

    @Test
    void deleteItem_ValidItemPedido_ReturnsItemPedido() {
        UUID existingItemPedidoId = UUID.randomUUID();
        ItemPedido existingItemPedido = new ItemPedido();
        existingItemPedido.setId(existingItemPedidoId);

        itemPedidoService.deletarItem(existingItemPedido);

        verify(itemPedidoRepository).delete(existingItemPedido);
    }

    @Test
    void aumentarQuantidadeProduto_ValidItemPedido_ReturnsItemPedido() {
        UUID existingItemPedidoId = UUID.randomUUID();
        ItemPedido existingItemPedido = ItemPedidoDTO.toEntity(montarItemPedidoDTO());
        existingItemPedido.setId(existingItemPedidoId);

        when(itemPedidoRepository.findById(existingItemPedidoId)).thenReturn(Optional.of(existingItemPedido));
        when(itemPedidoRepository.save(existingItemPedido)).thenReturn(existingItemPedido);

        ItemPedido result = itemPedidoService.aumentarQuantidadeProduto(1, existingItemPedidoId);

        Assertions.assertEquals(existingItemPedidoId, result.getId());
        Assertions.assertTrue(result.getQuantidade() > 0);
    }

    @Test
    void aumentarQuantidadeProduto_ValorNegativo_ThrowsPedidoException() {
        UUID existingItemPedidoId = UUID.randomUUID();
        ItemPedido existingItemPedido = ItemPedidoDTO.toEntity(montarItemPedidoDTO());
        existingItemPedido.setId(existingItemPedidoId);

        when(itemPedidoRepository.findById(existingItemPedidoId)).thenReturn(Optional.of(existingItemPedido));

        assertThrows(PedidoException.class, () -> itemPedidoService.aumentarQuantidadeProduto(-1, existingItemPedidoId));
    }

    @Test
    void diminuirQuantidadeProduto_ValidItemPedido_ReturnsItemPedido() {
        UUID existingItemPedidoId = UUID.randomUUID();
        ItemPedido existingItemPedido = new ItemPedido();
        existingItemPedido.setId(existingItemPedidoId);
        existingItemPedido.setQuantidade(2);

        when(itemPedidoRepository.findById(existingItemPedidoId)).thenReturn(Optional.of(existingItemPedido));
        when(itemPedidoRepository.save(existingItemPedido)).thenReturn(existingItemPedido);

        ItemPedido result = itemPedidoService.diminuirQuantidadeProduto(1, existingItemPedidoId);

        Assertions.assertEquals(existingItemPedidoId, result.getId());
        Assertions.assertTrue(result.getQuantidade() > 0);
    }

    @Test
    void diminuirQuantidadeProduto_ValorNegativo_ThrowsPedidoException() {
        UUID existingItemPedidoId = UUID.randomUUID();
        ItemPedido existingItemPedido = new ItemPedido();
        existingItemPedido.setId(existingItemPedidoId);

        when(itemPedidoRepository.findById(existingItemPedidoId)).thenReturn(Optional.of(existingItemPedido));

        assertThrows(PedidoException.class, () -> itemPedidoService.diminuirQuantidadeProduto(-1, existingItemPedidoId));
    }
}
