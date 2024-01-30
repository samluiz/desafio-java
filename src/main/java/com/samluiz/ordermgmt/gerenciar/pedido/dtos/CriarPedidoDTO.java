package com.samluiz.ordermgmt.gerenciar.pedido.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public class CriarPedidoDTO {

    @NotNull(message = "O campo 'produtos' não pode ser nulo.")
    private List<UUID> produtos;

    public List<UUID> getProdutos() {
        return produtos;
    }
}
