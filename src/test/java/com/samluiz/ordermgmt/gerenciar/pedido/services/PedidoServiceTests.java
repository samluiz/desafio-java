package com.samluiz.ordermgmt.gerenciar.pedido.services;

import com.samluiz.ordermgmt.common.exceptions.PedidoException;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.gerenciar.pedido.dtos.CriarOuAdicionarPedidoDTO;
import com.samluiz.ordermgmt.gerenciar.pedido.dtos.ItemPedidoDTO;
import com.samluiz.ordermgmt.gerenciar.pedido.models.ItemPedido;
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

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.samluiz.ordermgmt.utils.ControllerTestUtils.montarProduto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PedidoServiceTests {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ItemPedidoService itemPedidoService;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void findById_ExistingId_ReturnsPedido() {
        UUID existingPedidoId = UUID.randomUUID();
        Pedido existingPedido = new Pedido();
        existingPedido.setId(existingPedidoId);

        when(pedidoRepository.findById(existingPedidoId)).thenReturn(Optional.of(existingPedido));

        Pedido result = pedidoService.buscarPedidoPorId(existingPedidoId);

        assertEquals(existingPedidoId, result.getId());
    }

    @Test
    void findById_NonExistingId_ThrowsRecursoNaoEncontradoException() {
        UUID nonExistingPedidoId = UUID.randomUUID();

        when(pedidoRepository.findById(nonExistingPedidoId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> pedidoService.buscarPedidoPorId(nonExistingPedidoId));
    }

    @Test
    void findAll_ReturnsPageOfPedidos() {
        Page<Pedido> mockPage = Page.empty();

        when(pedidoRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<Pedido> result = pedidoService.buscarTodos(Pageable.unpaged());

        assertEquals(mockPage, result);
    }

    @Test
    void criarPedido_ValidCriarPedidoDTO_ReturnsCreatedPedido() {
        UUID produtoId = UUID.randomUUID();
        ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO();
        itemPedidoDTO.setProduto(produtoId);
        itemPedidoDTO.setQuantidade(1);
        CriarOuAdicionarPedidoDTO criarOuAdicionarPedidoDTO = new CriarOuAdicionarPedidoDTO();
        criarOuAdicionarPedidoDTO.setItens(Set.of(itemPedidoDTO));

        Produto produto = new Produto();
        produto.setId(produtoId);

        Pedido createdPedido = new Pedido();

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(itemPedidoService.salvarItem(any(ItemPedido.class))).thenReturn(mock(ItemPedido.class));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(createdPedido);

        Pedido result = pedidoService.criarPedido(criarOuAdicionarPedidoDTO);

        assertNotNull(result);
        assertEquals(createdPedido, result);
    }

    @Test
    void criarPedido_InvalidCriarPedidoDTO_ThrowsPedidoException() {
        CriarOuAdicionarPedidoDTO criarOuAdicionarPedidoDTO = new CriarOuAdicionarPedidoDTO();

        assertThrows(PedidoException.class, () -> pedidoService.criarPedido(criarOuAdicionarPedidoDTO));
    }

    @Test
    void criarPedido_DataAccessException_ThrowsPedidoException() {
        when(pedidoRepository.save(any(Pedido.class))).thenThrow(PermissionDeniedDataAccessException.class);

        assertThrows(PedidoException.class, () -> pedidoService.criarPedido(mock(CriarOuAdicionarPedidoDTO.class)));
    }

    @Test
    void adicionarNovoItem_ValidCriarPedidoDTO_ReturnsCreatedPedido() {
        UUID produtoId = UUID.randomUUID();
        ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO();
        itemPedidoDTO.setProduto(produtoId);
        itemPedidoDTO.setQuantidade(1);
        CriarOuAdicionarPedidoDTO criarOuAdicionarPedidoDTO = new CriarOuAdicionarPedidoDTO();
        criarOuAdicionarPedidoDTO.setItens(Set.of(itemPedidoDTO));

        Produto produto = new Produto();
        produto.setId(produtoId);

        Pedido existingPedido = new Pedido();
        existingPedido.setId(UUID.randomUUID());

        Pedido updatedPedido = new Pedido();
        updatedPedido.setId(existingPedido.getId());

        when(pedidoRepository.findById(existingPedido.getId())).thenReturn(Optional.of(existingPedido));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(itemPedidoService.salvarItem(any(ItemPedido.class))).thenReturn(mock(ItemPedido.class));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(updatedPedido);

        Pedido result = pedidoService.adicionarNovoItem(existingPedido.getId(), criarOuAdicionarPedidoDTO);

        assertNotNull(result);
        assertEquals(updatedPedido, result);
    }

    @Test
    void adicionarNovoItem_InvalidCriarPedidoDTO_ThrowsPedidoException() {
        UUID existingPedidoId = UUID.randomUUID();
        CriarOuAdicionarPedidoDTO criarOuAdicionarPedidoDTO = new CriarOuAdicionarPedidoDTO();

        when(pedidoRepository.findById(existingPedidoId)).thenReturn(Optional.of(mock(Pedido.class)));

        assertThrows(PedidoException.class, () -> pedidoService.adicionarNovoItem(existingPedidoId, criarOuAdicionarPedidoDTO));
    }

    @Test
    void adicionarNovoItem_DataAccessException_ThrowsPedidoException() {
        UUID existingPedidoId = UUID.randomUUID();

        when(pedidoRepository.findById(existingPedidoId)).thenReturn(Optional.of(mock(Pedido.class)));
        when(pedidoRepository.save(any(Pedido.class))).thenThrow(PermissionDeniedDataAccessException.class);

        assertThrows(PedidoException.class, () -> pedidoService.adicionarNovoItem(existingPedidoId, mock(CriarOuAdicionarPedidoDTO.class)));
    }

    @Test
    void removerItem_ValidPedidoIdAndItemPedidoId_ReturnsUpdatedPedido() {
        UUID pedidoId = UUID.randomUUID();
        UUID itemPedidoId = UUID.randomUUID();

        Pedido existingPedido = new Pedido();
        existingPedido.setId(pedidoId);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(itemPedidoId);
        itemPedido.setPedido(existingPedido);

        existingPedido.addItem(itemPedido);

        Pedido updatedPedido = new Pedido();

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(existingPedido));
        when(itemPedidoService.buscarItemPorId(itemPedidoId)).thenReturn(itemPedido);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(updatedPedido);

        Pedido result = pedidoService.removerItem(pedidoId, itemPedidoId);

        assertNotNull(result);
        assertEquals(updatedPedido, result);
    }

    @Test
    void testAdicionarItem_WhenSameProdutoInPedido_ThenSumQuantities() {
        UUID produtoId = UUID.randomUUID();
        UUID produtoId2 = UUID.randomUUID();
        ItemPedidoDTO itemPedidoDTO1 = new ItemPedidoDTO();
        itemPedidoDTO1.setProduto(produtoId);
        itemPedidoDTO1.setQuantidade(2);

        ItemPedidoDTO itemPedidoDTO2 = new ItemPedidoDTO();
        itemPedidoDTO2.setProduto(produtoId2);
        itemPedidoDTO2.setQuantidade(3);

        CriarOuAdicionarPedidoDTO dto = new CriarOuAdicionarPedidoDTO();
        dto.setItens(Set.of(itemPedidoDTO1, itemPedidoDTO2));

        Produto produto = montarProduto();
        produto.setId(produtoId);

        Produto produto2 = montarProduto();
        produto2.setId(produtoId2);

        ItemPedido existingItem = new ItemPedido();
        existingItem.setId(UUID.randomUUID());
        existingItem.setProduto(produto);
        existingItem.setQuantidade(5);

        Pedido pedido = new Pedido();
        pedido.addItem(existingItem);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoRepository.findById(produtoId2)).thenReturn(Optional.of(produto2));
        when(pedidoRepository.save(any())).thenReturn(pedido);
        when(itemPedidoService.salvarItem(any())).thenReturn(existingItem);

        Pedido result = pedidoService.adicionarItem(dto, pedido);

        assertThat(result.getItens()).hasSize(1);

        ItemPedido updatedItem = result.getItens().stream()
                .filter(i -> i.getProduto().getId().equals(produtoId))
                .findFirst()
                .orElse(null);

        assertThat(updatedItem).isNotNull();
        assertThat(updatedItem.getQuantidade()).isEqualTo(5 + itemPedidoDTO1.getQuantidade());
    }
}
