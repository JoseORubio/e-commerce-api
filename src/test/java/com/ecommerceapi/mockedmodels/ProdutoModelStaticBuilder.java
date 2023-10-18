package com.ecommerceapi.mockedmodels;

import com.ecommerceapi.models.ProdutoModel;

import java.math.BigDecimal;

public class ProdutoModelStaticBuilder {
    private static ProdutoModel produtoModel = new ProdutoModel();

    public static ProdutoModel getProdutoModelSemId(){
        produtoModel.setId(null);
        produtoModel.setNome("Blusa Z");
        produtoModel.setQuantidade_estoque(10);
        produtoModel.setPreco(BigDecimal.valueOf(15.4));
        return produtoModel;
    }
}
