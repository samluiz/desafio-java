package com.samluiz.ordermgmt.produto.repositories;

import com.samluiz.ordermgmt.produto.models.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    Page<Produto> findAll(Pageable pageable);
}
