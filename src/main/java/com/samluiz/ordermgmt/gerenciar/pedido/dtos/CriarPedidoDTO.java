package com.samluiz.ordermgmt.gerenciar.pedido.dtos;

import java.util.List;
import java.util.UUID;

public class CriarPedidoDTO {

    private List<UUID> produtos;

    public List<UUID> getProdutos() {
        return produtos;
    }
}
