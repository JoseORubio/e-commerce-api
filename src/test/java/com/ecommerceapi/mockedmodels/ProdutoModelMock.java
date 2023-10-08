package com.ecommerceapi.mockedmodels;

import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.utils.ConversorUUID;

import java.math.BigDecimal;
import java.util.UUID;

public class ProdutoModelMock extends ProdutoModel {

    public ProdutoModelMock() {
        UUID uuid = ConversorUUID.converteUUID("b77a0ee4-3ec2-479b-9640-a215a6cab4a3");
        String nome = "Blusa";
        int quantidade = 10;
        BigDecimal preco = BigDecimal.valueOf(15.4);

        this.setId(uuid);
        this.setNome(nome);
        this.setQuantidade_estoque(quantidade);
        this.setPreco(preco);
    }


}
