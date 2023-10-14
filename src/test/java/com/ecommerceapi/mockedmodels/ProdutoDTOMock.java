package com.ecommerceapi.mockedmodels;

import com.ecommerceapi.dtos.ProdutoDTO;

import java.math.BigDecimal;

public class ProdutoDTOMock extends ProdutoDTO {

    public ProdutoDTOMock() {
        String nome = "Blusa";
        String quantidade = "10";
        String preco = "15.4";

        this.setNome(nome);
        this.setQuantidade_estoque(quantidade);
        this.setPreco(preco);
    }
}
