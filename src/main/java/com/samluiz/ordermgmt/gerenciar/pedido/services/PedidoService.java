package com.samluiz.ordermgmt.gerenciar.pedido.services;

import com.samluiz.ordermgmt.common.exceptions.PedidoException;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.gerenciar.pedido.dtos.CriarOuAdicionarPedidoDTO;
import com.samluiz.ordermgmt.gerenciar.pedido.models.ItemPedido;
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
    private final ItemPedidoService itemPedidoService;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ItemPedidoService itemPedidoService, ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoService = itemPedidoService;
        this.produtoRepository = produtoRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    public Pedido buscarPedidoPorId(UUID id) {
        try {
            return pedidoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(id));
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar pedido com id {} -> Erro: {}", id, e.getMessage());
            throw e;
        }
    }

    public Page<Pedido> buscarTodos(Pageable pageable) {
        try {
            return pedidoRepository.findAll(pageable);
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar pedidos -> Erro: {}", e.getMessage());
            throw e;
        }
    }

    public Pedido criarPedido(CriarOuAdicionarPedidoDTO dto) {
        try {
            if (dto.getItens() == null || dto.getItens().isEmpty()) {
                logger.info("Nenhum item informado.");
                throw new PedidoException("Nenhum item informado.");
            }
            Pedido pedido = new Pedido();
            return adicionarItem(dto, pedido);
        } catch (DataAccessException e) {
            logger.error("Erro ao criar pedido -> Erro: {}", e.getMessage());
            throw e;
        }
    }

    public Pedido adicionarNovoItem(UUID id, CriarOuAdicionarPedidoDTO dto) {
        try {
            if (dto.getItens() == null || dto.getItens().isEmpty()) {
                logger.info("Nenhum item informado.");
                throw new PedidoException("Nenhum item informado.");
            }
            Pedido pedido = buscarPedidoPorId(id);
            return adicionarItem(dto, pedido);
        } catch (DataAccessException e) {
            logger.error("Erro ao adicionar item ao pedido com id {} -> Erro: {}", id, e.getMessage());
            throw e;
        }
    }

    public Pedido removerItem(UUID pedidoId, UUID itemPedidoId) {
        try {
            Pedido pedido = buscarPedidoPorId(pedidoId);
            ItemPedido item = itemPedidoService.buscarItemPorId(itemPedidoId);
            pedido.removeItem(item);
            itemPedidoService.deletarItem(item);
            return pedidoRepository.save(pedido);
        } catch (DataAccessException e) {
            logger.error("Erro ao remover item ao pedido com id {} -> Erro: {}", itemPedidoId, e.getMessage());
            throw e;
        }
    }

    private Pedido adicionarItem(CriarOuAdicionarPedidoDTO dto, Pedido pedido) {
        ItemPedido item = new ItemPedido();
        dto.getItens().forEach(i -> {
            Optional<Produto> produto = produtoRepository.findById(i.getProduto());
            if (produto.isEmpty()) {
                logger.info("Produto com id {} não encontrado.", i.getProduto());
            } else {
                Produto produtoAtual = produto.get();
                item.setProduto(produtoAtual);
                item.setQuantidade(i.getQuantidade());
                item.setPedido(pedido);
                pedido.addItem(itemPedidoService.salvarItem(item));
            }
        });
        return pedidoRepository.save(pedido);
    }

    public Pedido aumentarQuantidadeProdutoItem(UUID itemPedidoId, Integer quantidade) {
        ItemPedido itemAtualizado = itemPedidoService.aumentarQuantidadeProduto(quantidade, itemPedidoId);
        return itemAtualizado.getPedido();
    }

    public Pedido diminuirQuantidadeProdutoItem(UUID itemPedidoId, Integer quantidade) {
        ItemPedido itemAtualizado = itemPedidoService.diminuirQuantidadeProduto(quantidade, itemPedidoId);
        return itemAtualizado.getPedido();
    }
}
