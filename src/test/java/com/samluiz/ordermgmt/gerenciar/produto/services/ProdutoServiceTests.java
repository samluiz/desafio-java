package com.samluiz.ordermgmt.gerenciar.produto.services;

import com.samluiz.ordermgmt.common.exceptions.ProdutoException;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
import com.samluiz.ordermgmt.gerenciar.produto.repositories.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTests {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private UUID existingProductId;
    private UUID nonExistingProductId;

    @BeforeEach
    void setUp() {
        existingProductId = UUID.randomUUID();
        nonExistingProductId = UUID.randomUUID();
    }

    @Test
    void findById_ExistingId_ReturnsProduct() {
        Produto existingProduto = new Produto();
        when(produtoRepository.findById(existingProductId)).thenReturn(Optional.of(existingProduto));

        Produto result = produtoService.findById(existingProductId);

        assertSame(existingProduto, result);
    }

    @Test
    void findById_NonExistingId_ThrowsRecursoNaoEncontradoException() {
        when(produtoRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> produtoService.findById(nonExistingProductId));
    }

    @Test
    void findAll_NoException_ReturnsPageOfProducts() {
        Page<Produto> mockPage = mock(Page.class);
        when(produtoRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        Page<Produto> result = produtoService.findAll(mock(Pageable.class));

        assertSame(mockPage, result);
    }

    @Test
    void findAll_DataAccessException_ThrowsProdutoException() {
        when(produtoRepository.findAll(any(Pageable.class))).thenThrow(PermissionDeniedDataAccessException.class);

        assertThrows(PermissionDeniedDataAccessException.class, () -> produtoService.findAll(mock(Pageable.class)));
    }

    @Test
    void create_NoException_ReturnsCreatedProduct() {
        Produto mockProduto = mock(Produto.class);
        when(produtoRepository.save(any(Produto.class))).thenReturn(mockProduto);

        Produto result = produtoService.create(mockProduto);

        assertSame(mockProduto, result);
    }

    @Test
    void create_DataAccessException_ThrowsProdutoException() {
        when(produtoRepository.save(any(Produto.class))).thenThrow(PermissionDeniedDataAccessException.class);

        assertThrows(PermissionDeniedDataAccessException.class, () -> produtoService.create(mock(Produto.class)));
    }

    @Test
    void update_ExistingId_ReturnsUpdatedProduct() {
        UUID productId = UUID.randomUUID();
        Produto existingProduto = new Produto();
        Produto updatedProduto = new Produto();
        when(produtoRepository.findById(productId)).thenReturn(Optional.of(existingProduto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(updatedProduto);

        Produto result = produtoService.update(mock(Produto.class), productId);

        assertSame(updatedProduto, result);
    }

    @Test
    void update_NonExistingId_ThrowsRecursoNaoEncontradoException() {
        when(produtoRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> produtoService.update(mock(Produto.class), nonExistingProductId));
    }

    @Test
    void update_DataAccessException_ThrowsProdutoException() {
        UUID productId = UUID.randomUUID();
        when(produtoRepository.findById(productId)).thenReturn(Optional.of(mock(Produto.class)));
        when(produtoRepository.save(any(Produto.class))).thenThrow(PermissionDeniedDataAccessException.class);

        assertThrows(PermissionDeniedDataAccessException.class, () -> produtoService.update(mock(Produto.class), productId));
    }

    @Test
    void delete_ExistingId_DeletesProduct() {
        UUID productId = UUID.randomUUID();
        Produto existingProduto = new Produto();
        when(produtoRepository.findById(productId)).thenReturn(Optional.of(existingProduto));

        produtoService.delete(productId);

        verify(produtoRepository).save(existingProduto);
    }

    @Test
    void delete_NonExistingId_ThrowsRecursoNaoEncontradoException() {
        when(produtoRepository.findById(nonExistingProductId)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> produtoService.delete(nonExistingProductId));
    }

    @Test
    void delete_DataAccessException_ThrowsProdutoException() {
        UUID productId = UUID.randomUUID();
        when(produtoRepository.findById(productId)).thenReturn(Optional.of(mock(Produto.class)));
        when(produtoRepository.save(any(Produto.class))).thenThrow(PermissionDeniedDataAccessException.class);

        assertThrows(PermissionDeniedDataAccessException.class, () -> produtoService.delete(productId));
    }
}
