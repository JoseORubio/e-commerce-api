package com.ecommerceapi.mockedmodels;

import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.utils.ConversorUUID;


public class CarrinhoModelMock extends CarrinhoModel {

    public CarrinhoModelMock() {
        UsuarioModelMock usuarioModel = new UsuarioModelMock();
        ProdutoModelMock produtoModel = new ProdutoModelMock();
        CarrinhoModel carrinhoModel = new CarrinhoModel(usuarioModel, produtoModel, produtoModel.getQuantidade_estoque());

        this.setId(ConversorUUID.converteUUID("b77a0ee4-3ec2-479b-9640-a215a6cab4a3"));
        this.setUsuario(carrinhoModel.getUsuario());
        this.setProduto(carrinhoModel.getProduto());
        this.setQuantidade(carrinhoModel.getQuantidade());
        this.setValorTotalProduto(carrinhoModel.getValorTotalProduto());
    }

}
