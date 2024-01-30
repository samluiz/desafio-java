package com.samluiz.ordermgmt.gerenciar.pedido.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;

    public ItemPedido() {
    }

    public ItemPedido(Pedido pedido, Produto produto, Integer quantidade) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void aumentarQuantidadeProduto(Integer quantidade) {
        this.quantidade += quantidade;
    }

    public void diminuirQuantidadeProduto(Integer quantidade) {
        this.quantidade -= quantidade;
    }

    public Double getSubTotal() {
        return this.quantidade * this.produto.getPreco();
    }
}
