package com.samluiz.ordermgmt.gerenciar.produto.repositories;

import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
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

import static com.samluiz.ordermgmt.utils.ControllerTestUtils.montarProduto;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class ProdutoRepositoryTests {

    @Autowired
    private ProdutoRepository produtoRepository;

    private UUID existingProductId;
    private UUID nonExistingProductId;

    @Test
    void findById_ExistingId_ReturnsOptionalOfProduto() {
        Produto existingProduto = new Produto();
        existingProductId = produtoRepository.save(existingProduto).getId();

        Optional<Produto> result = produtoRepository.findById(existingProductId);

        assertTrue(result.isPresent());
        assertEquals(existingProduto, result.get());
    }

    @Test
    void findById_NonExistingId_ReturnsEmptyOptional() {
        nonExistingProductId = UUID.randomUUID();

        Optional<Produto> result = produtoRepository.findById(nonExistingProductId);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_NoException_ReturnsPageOfProducts() {
        Produto produto1 = montarProduto();
        Produto produto2 = montarProduto();
        produtoRepository.saveAll(List.of(produto1, produto2));

        Page<Produto> result = produtoRepository.findAll(PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void save_NoException_ReturnsSavedProduto() {
        Produto produtoToSave = montarProduto();

        Produto result = produtoRepository.save(produtoToSave);

        assertNotNull(result.getId());
        assertEquals(produtoToSave.getNome(), result.getNome());
    }

    @Test
    @Transactional
    void saveWithList_NoException_ReturnsSavedProdutoList() {
        List<Produto> produtosToSave = List.of(montarProduto(), montarProduto());

        List<Produto> result = produtoRepository.saveAll(produtosToSave);

        assertEquals(2, result.size());
        assertNotNull(result.get(0).getId());
    }
}
