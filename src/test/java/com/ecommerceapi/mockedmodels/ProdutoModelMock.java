package com.ecommerceapi.mockedmodels;

import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.utils.ConversorUUID;

import java.math.BigDecimal;

public class ProdutoModelMock extends ProdutoModel {

    public ProdutoModelMock() {
        this.setId(ConversorUUID.converteUUID("b77a0ee4-3ec2-479b-9640-a215a6cab4a3"));
        this.setNome("Blusa Z");
        this.setQuantidade_estoque(10);
        this.setPreco(BigDecimal.valueOf(15.4));
    }


}
