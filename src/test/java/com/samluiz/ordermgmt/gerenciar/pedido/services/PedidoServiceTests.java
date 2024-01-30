package com.samluiz.ordermgmt.gerenciar.pedido.services;

import com.samluiz.ordermgmt.common.exceptions.PedidoException;
import com.samluiz.ordermgmt.common.exceptions.ProdutoException;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.gerenciar.pedido.dtos.CriarPedidoDTO;
import com.samluiz.ordermgmt.gerenciar.pedido.models.Pedido;
import com.samluiz.ordermgmt.gerenciar.pedido.repositories.PedidoRepository;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
import com.samluiz.ordermgmt.gerenciar.produto.repositories.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PedidoServiceTests {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void findById_ExistingId_ReturnsPedido() {
        UUID existingPedidoId = UUID.randomUUID();
        Pedido existingPedido = new Pedido();
        existingPedido.setId(existingPedidoId);

        when(pedidoRepository.findById(existingPedidoId)).thenReturn(Optional.of(existingPedido));

        Pedido result = pedidoService.findById(existingPedidoId);

        assertEquals(existingPedidoId, result.getId());
    }

    @Test
    void findById_NonExistingId_ThrowsRecursoNaoEncontradoException() {
        UUID nonExistingPedidoId = UUID.randomUUID();

        when(pedidoRepository.findById(nonExistingPedidoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> pedidoService.findById(nonExistingPedidoId));
    }

    @Test
    void findAll_ReturnsPageOfPedidos() {
        Page<Pedido> mockPage = Page.empty();

        when(pedidoRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<Pedido> result = pedidoService.findAll(Pageable.unpaged());

        assertEquals(mockPage, result);
    }

    @Test
    void create_ValidCriarPedidoDTO_ReturnsCreatedPedido() {
        UUID produtoId = UUID.randomUUID();
        CriarPedidoDTO criarPedidoDTO = new CriarPedidoDTO();
        criarPedidoDTO.setProdutos(List.of(produtoId));

        Produto produto = new Produto();
        produto.setId(produtoId);

        Pedido createdPedido = new Pedido();

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(createdPedido);

        Pedido result = pedidoService.create(criarPedidoDTO);

        assertNotNull(result);
        assertEquals(createdPedido, result);
    }

    @Test
    void create_InvalidCriarPedidoDTO_ThrowsPedidoException() {
        CriarPedidoDTO criarPedidoDTO = new CriarPedidoDTO();

        assertThrows(PedidoException.class, () -> pedidoService.create(criarPedidoDTO));
    }

    @Test
    void create_DataAccessException_ThrowsPedidoException() {
        when(pedidoRepository.save(any(Pedido.class))).thenThrow(PermissionDeniedDataAccessException.class);

        assertThrows(PedidoException.class, () -> pedidoService.create(mock(CriarPedidoDTO.class)));
    }
}
