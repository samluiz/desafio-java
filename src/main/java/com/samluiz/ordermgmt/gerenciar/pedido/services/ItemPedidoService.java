package com.samluiz.ordermgmt.gerenciar.pedido.services;

import com.samluiz.ordermgmt.common.exceptions.PedidoException;
import com.samluiz.ordermgmt.common.exceptions.RecursoNaoEncontradoException;
import com.samluiz.ordermgmt.gerenciar.pedido.models.ItemPedido;
import com.samluiz.ordermgmt.gerenciar.pedido.repositories.ItemPedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(ItemPedidoService.class);

    public ItemPedido buscarItemPorId(UUID id) {
        try {
            return itemPedidoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException(id));
        } catch (DataAccessException e) {
            logger.error("Erro ao buscar item com id {} -> Erro: {}", id, e.getMessage());
            throw e;
        }
    }

    public ItemPedido salvarItem(ItemPedido itemPedido) {
        try {
            return itemPedidoRepository.save(itemPedido);
        } catch (DataAccessException e) {
            logger.error("Erro ao salvar item pedido -> Erro: {}", e.getMessage());
            throw e;
        }
    }

    public void deletarItem(ItemPedido itemPedido) {
        try {
            itemPedidoRepository.delete(itemPedido);
        } catch (DataAccessException e) {
            logger.error("Erro ao deletar item pedido -> Erro: {}", e.getMessage());
            throw new PedidoException("Erro ao deletar item pedido.");
        }
    }

    public ItemPedido aumentarQuantidadeProduto(Integer quantidade, UUID itemId) {
        if (quantidade <= 0) {
            logger.info("Quantidade deve ser maior que zero.");
            throw new PedidoException("Quantidade deve ser maior que zero.");
        }

        ItemPedido item = buscarItemPorId(itemId);
        item.aumentarQuantidadeProduto(quantidade);
        return salvarItem(item);
    }

    public ItemPedido diminuirQuantidadeProduto(Integer quantidade, UUID itemId) {
        if (quantidade <= 0) {
            logger.info("Quantidade deve ser maior que zero.");
            throw new PedidoException("Quantidade deve ser maior que zero.");
        }

        ItemPedido item = buscarItemPorId(itemId);

        if (item.getQuantidade() - quantidade <= 0) {
            logger.info("A quantidade não pode ser menor ou igual a zero após diminuir.");
            throw new PedidoException("A quantidade não pode ser menor ou igual a zero após diminuir.");
        }
        item.diminuirQuantidadeProduto(quantidade);
        return salvarItem(item);
    }
}
