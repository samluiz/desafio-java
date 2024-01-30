package com.samluiz.ordermgmt.gerenciar.produto.services;

import com.samluiz.ordermgmt.common.exceptions.ProdutoException;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
import com.samluiz.ordermgmt.gerenciar.produto.repositories.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

    public Produto findById(UUID id) {
        try {
            return produtoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(id));
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar produto com id {} -> Erro: {}", id, e.getMessage());
            throw new ProdutoException("Erro ao buscar produto com id " + id);
        }
    }

    public Page<Produto> findAll(Pageable pageable) {
        try {
            return produtoRepository.findAll(pageable);
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar produtos -> Erro: {}", e.getMessage());
            throw new ProdutoException("Erro ao buscar produtos.");
        }
    }

    public Produto create(Produto obj) {
        try {
            return produtoRepository.save(obj);
        } catch (DataAccessException e) {
            logger.error("Erro ao criar produto -> Erro: {}", e.getMessage());
            throw new ProdutoException("Erro ao criar produto.");
        }
    }

    public Produto update(Produto obj, UUID id) {
        try {
            Produto entity = findById(id);
            entity.setNome(obj.getNome());
            entity.setPreco(obj.getPreco());
            entity.setCategoria(obj.getCategoria());
            return produtoRepository.save(entity);
        } catch (DataAccessException e) {
            logger.error("Erro ao atualizar produto -> Erro: {}", e.getMessage());
            throw new ProdutoException("Erro ao atualizar produto.");
        }
    }

    public void delete(UUID id) {
        try {
            produtoRepository.delete(findById(id));
        } catch (DataAccessException e) {
            logger.error("Erro ao deletar produto -> Erro: {}", e.getMessage());
            throw new ProdutoException("Erro ao deletar produto.");
        }
    }
}
