package com.samluiz.ordermgmt.gerenciar.pedido.dtos;

import com.samluiz.ordermgmt.gerenciar.pedido.models.ItemPedido;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class ItemPedidoDTO {

    @NotNull(message = "O campo 'produto' não pode ser nulo.")
    private UUID produto;

    @NotNull(message = "O campo 'quantidade' não pode ser nulo.")
    @Positive(message = "O campo 'quantidade' deve ser um número positivo.")
    private Integer quantidade;

    public ItemPedidoDTO() {
    }

    public UUID getProduto() {
        return produto;
    }

    public void setProduto(UUID produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public static ItemPedido toEntity(ItemPedidoDTO itemPedidoDTO) {
        Produto produto = new Produto();
        produto.setId(itemPedidoDTO.getProduto());
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(itemPedidoDTO.getQuantidade());
        return itemPedido;
    }
}
