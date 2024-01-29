package com.samluiz.ordermgmt.produto.services;

import com.samluiz.ordermgmt.common.exceptions.ResourceNotFoundException;
import com.samluiz.ordermgmt.produto.models.Produto;
import com.samluiz.ordermgmt.produto.repositories.ProdutoRepository;
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

    public Produto findById(UUID id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Page<Produto> findAll(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public Produto create(Produto obj) {
        return produtoRepository.save(obj);
    }

    public Produto update(Produto obj, UUID id) {
        Produto entity = findById(id);
        entity.setNome(obj.getNome());
        entity.setPreco(obj.getPreco());
        entity.setCategoria(obj.getCategoria());
        return produtoRepository.save(entity);
    }

    public void delete(UUID id) {
        produtoRepository.delete(findById(id));
    }
}
