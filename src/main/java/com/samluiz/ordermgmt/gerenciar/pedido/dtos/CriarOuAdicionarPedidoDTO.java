package com.samluiz.ordermgmt.gerenciar.pedido.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class CriarOuAdicionarPedidoDTO {

    @NotNull(message = "O campo 'itens' não pode ser nulo.")
    private Set<ItemPedidoDTO> itens;

    public CriarOuAdicionarPedidoDTO() {
    }

    public Set<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(Set<ItemPedidoDTO> itens) {
        this.itens = itens;
    }
}
