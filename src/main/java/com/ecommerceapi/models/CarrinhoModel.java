package com.ecommerceapi.models;

import java.math.BigDecimal;

public class CarrinhoModel {
    private ProdutoModel produtoModel;
    private int quantidade;
    private BigDecimal valorTotalProduto;

    public CarrinhoModel(ProdutoModel produtoModel, int quantidade) {
        this.produtoModel = produtoModel;
        this.quantidade = quantidade;
        this.valorTotalProduto = produtoModel.getPreco().multiply(BigDecimal.valueOf(quantidade));
    }

    public ProdutoModel getProdutoModel() {
        return produtoModel;
    }

    public void setProdutoModel(ProdutoModel produtoModel) {
        this.produtoModel = produtoModel;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        this.valorTotalProduto = produtoModel.getPreco().multiply(BigDecimal.valueOf(quantidade));
    }

    public BigDecimal getValorTotalProduto() {
        return valorTotalProduto;
    }

    public void setValorTotalProduto(BigDecimal valorTotalProduto) {
        this.valorTotalProduto = valorTotalProduto;
    }
}
