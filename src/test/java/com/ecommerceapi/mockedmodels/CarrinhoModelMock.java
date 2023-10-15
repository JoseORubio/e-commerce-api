package com.ecommerceapi.mockedmodels;

import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.utils.ConversorUUID;


public class CarrinhoModelMock extends CarrinhoModel {
    private CarrinhoModel carrinhoModel;

    public CarrinhoModelMock() {
        UsuarioModelMock usuarioModel = new UsuarioModelMock();
        ProdutoModelMock produtoModel = new ProdutoModelMock();
        carrinhoModel = new CarrinhoModel(usuarioModel, produtoModel, produtoModel.getQuantidade_estoque());
        carrinhoModel.setId(ConversorUUID.converteUUID("b77a0ee4-3ec2-479b-9640-a215a6cab4a3"));
    }

    public CarrinhoModel construir() {
        return carrinhoModel;
    }
}
