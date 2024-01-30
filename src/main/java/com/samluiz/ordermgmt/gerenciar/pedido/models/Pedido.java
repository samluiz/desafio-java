package com.samluiz.ordermgmt.gerenciar.pedido.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToMany
    @JoinTable(name = "tb_pedido_produto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private List<Produto> produtos = new ArrayList<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'", timezone = "UTC")
    @Column(name = "created_at", updatable=false)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss'Z'", timezone = "UTC")
    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public Pedido() {
    }

    public Pedido(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void addProduto(Produto produto) {
        this.produtos.add(produto);
        produto.addPedido(this);
    }

    public void removeProduto(Produto produto) {
        this.produtos.remove(produto);
    }

    public Double getTotal() {
        return produtos.stream().mapToDouble(Produto::getPreco).sum();
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", produtos=" + produtos +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
