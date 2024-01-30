package com.samluiz.ordermgmt.gerenciar.produto.enums;

public enum Categoria {
    BEBIDA("Bebida"),
    ENTRADA("Entrada"),
    PRATO_PRINCIPAL("Prato Principal"),
    SOBREMESA("Sobremesa");

    private String nome;

    Categoria(String nome) {
        this.nome = nome;
    }
}
