package com.samluiz.ordermgmt.common.utils;

import com.samluiz.ordermgmt.gerenciar.produto.enums.Categoria;
import com.samluiz.ordermgmt.gerenciar.produto.models.Produto;

public class ControllerTestUtils {

    public static Produto montarProduto() {
        Produto produto = new Produto();
        produto.setNome("Produto 1");
        produto.setPreco(10.00);
        produto.setCategoria(Categoria.PRATO_PRINCIPAL);
        return produto;
    }
}
