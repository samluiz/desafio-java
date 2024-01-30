package com.samluiz.ordermgmt.gerenciar.pedido.services;

import com.samluiz.ordermgmt.common.exceptions.PedidoException;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.gerenciar.pedido.dtos.CriarPedidoDTO;
import com.samluiz.ordermgmt.gerenciar.pedido.models.Pedido;
import com.samluiz.ordermgmt.gerenciar.pedido.repositories.PedidoRepository;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
import com.samluiz.ordermgmt.gerenciar.produto.repositories.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    public Pedido findById(UUID id) {
        try {
            return pedidoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(id));
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar pedido com id {} -> Erro: {}", id, e.getMessage());
            throw new PedidoException("Erro ao buscar pedido com id " + id);
        }
    }

    public Page<Pedido> findAll(Pageable pageable) {
        try {
            return pedidoRepository.findAll(pageable);
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar pedidos -> Erro: {}", e.getMessage());
            throw new PedidoException("Erro ao buscar pedidos.");
        }
    }

    public Pedido create(CriarPedidoDTO dto) {
        try {
            if (dto.getProdutos() == null || dto.getProdutos().isEmpty()) {
                logger.info("Nenhum produto informado.");
                throw new PedidoException("Nenhum produto informado.");
            }
            Pedido pedido = new Pedido();
            for (UUID produtoId : dto.getProdutos()) {
                Optional<Produto> produto = produtoRepository.findById(produtoId);
                if (produto.isEmpty()) {
                    logger.info("Produto com id {} não encontrado.", produtoId);
                } else {
                    Produto produtoAtual = produto.get();
                    pedido.addProduto(produtoAtual);
                }
            }
            return pedidoRepository.save(pedido);
        } catch (DataAccessException e) {
            logger.error("Erro ao criar pedido -> Erro: {}", e.getMessage());
            throw new PedidoException("Erro ao criar pedido.");
        }
    }
}
