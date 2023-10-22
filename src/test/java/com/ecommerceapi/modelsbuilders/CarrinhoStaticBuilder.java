package com.ecommerceapi.modelsbuilders;

import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.utils.ConversorUUID;

public class CarrinhoStaticBuilder {
    private static CarrinhoModel carrinhoModel = new CarrinhoModel();
    private static UsuarioModel usuarioModel = UsuarioStaticBuilder.getMockUsuarioModelComId();
    private static ProdutoModel produtoModel = ProdutoStaticBuilder.getMockProdutoModelComId();

    public static CarrinhoModel getMockCarrinhoModel(){
        carrinhoModel = new CarrinhoModel(usuarioModel, produtoModel, produtoModel.getQuantidadeEstoque());
        carrinhoModel.setId(ConversorUUID.converteUUID("b77a0ee4-3ec2-479b-9640-a215a6cab4a3"));

        return carrinhoModel;
    }
}
