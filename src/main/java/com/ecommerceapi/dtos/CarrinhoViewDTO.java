package com.ecommerceapi.dtos;

import com.ecommerceapi.models.ProdutoModel;

import java.math.BigDecimal;

public class CarrinhoViewDTO {

    private ProdutoModel produto;
    private int quantidade;
    private BigDecimal valorTotalProduto;

    public ProdutoModel getProduto() {
        return produto;
    }

    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorTotalProduto() {
        return valorTotalProduto;
    }

    public void setValorTotalProduto(BigDecimal valorTotalProduto) {
        this.valorTotalProduto = valorTotalProduto;
    }
}
