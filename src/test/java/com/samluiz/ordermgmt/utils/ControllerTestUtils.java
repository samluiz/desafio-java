package com.samluiz.ordermgmt.utils;

import com.samluiz.ordermgmt.auth.user.dtos.CreateUserDTO;
import com.samluiz.ordermgmt.auth.user.enums.Role;
import com.samluiz.ordermgmt.gerenciar.pedido.dtos.CriarPedidoDTO;
import com.samluiz.ordermgmt.gerenciar.produto.enums.Categoria;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ControllerTestUtils {

    public static Produto montarProduto() {
        Produto produto = new Produto();
        produto.setNome("Produto 1");
        produto.setPreco(10.00);
        produto.setCategoria(Categoria.PRATO_PRINCIPAL);
        return produto;
    }

    public static CriarPedidoDTO montarCriarPedidoDTO() {
        CriarPedidoDTO dto = new CriarPedidoDTO();
        dto.setProdutos(List.of(UUID.randomUUID()));
        return dto;
    }

    public static CreateUserDTO criarUsuario() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsername("admin");
        dto.setPassword("admin");
        dto.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_EDITOR, Role.ROLE_VIEWER));
        return dto;
    }
}
