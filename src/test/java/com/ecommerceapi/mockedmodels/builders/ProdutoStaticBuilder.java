package com.ecommerceapi.mockedmodels.builders;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.models.ProdutoModel;

import java.math.BigDecimal;

public class ProdutoStaticBuilder {
    private static ProdutoModel produtoModel = new ProdutoModel();
    private static ProdutoModel produtoModel2 = new ProdutoModel();
    private static ProdutoModel produtoModel3 = new ProdutoModel();
    private static ProdutoDTO produtoDto = new ProdutoDTO();

    private static String nome = "Blusa Z";
    private static String preco = "15.4";
    private static String quantidadeEstoque = "15";


    public static ProdutoModel getProdutoModelSemId(){
        produtoModel.setId(null);
        produtoModel.setNome(nome);
        produtoModel.setPreco(BigDecimal.valueOf(Double.parseDouble(preco)));
        produtoModel.setQuantidade_estoque(Integer.parseInt(quantidadeEstoque));
        return produtoModel;
    }
    public static ProdutoModel getProdutoModelSemId2(){
        produtoModel2.setId(null);
        produtoModel2.setNome("Tênis W");
        produtoModel2.setPreco(BigDecimal.valueOf(100));
        produtoModel2.setQuantidade_estoque(55);
        return produtoModel2;
    }
    public static ProdutoModel getProdutoModelSemId3(){
        produtoModel3.setId(null);
        produtoModel3.setNome("Meia Y");
        produtoModel3.setPreco(BigDecimal.valueOf(5));
        produtoModel3.setQuantidade_estoque(111);
        return produtoModel3;
    }

    public static ProdutoDTO getProdutoDto() {
        produtoDto.setNome(nome);
        produtoDto.setPreco(preco);
        produtoDto.setQuantidade_estoque(quantidadeEstoque);
        return produtoDto;
    }
    public static ProdutoDTO getProdutoDtoNomeInvalido() {
        produtoDto.setNome("*****");
        produtoDto.setPreco(quantidadeEstoque);
        produtoDto.setQuantidade_estoque(quantidadeEstoque);
        return produtoDto;
    }
    public static ProdutoDTO getProdutoDtoPrecoInvalido() {
        produtoDto.setNome(nome);
        produtoDto.setPreco("*****");
        produtoDto.setQuantidade_estoque(quantidadeEstoque);
        return produtoDto;
    }
    public static ProdutoDTO getProdutoDtoQuantidadeEstoqueInvalida() {
        produtoDto.setNome(nome);
        produtoDto.setPreco(preco);
        produtoDto.setQuantidade_estoque("*****");
        return produtoDto;
    }
    public static ProdutoDTO getProdutoDtoAtributosNulos() {
        produtoDto.setNome(null);
        produtoDto.setPreco(null);
        produtoDto.setQuantidade_estoque(null);
        return produtoDto;
    }
    public static ProdutoDTO getProdutoDtoStringsVazias() {
        produtoDto.setNome("");
        produtoDto.setPreco("");
        produtoDto.setQuantidade_estoque("");
        return produtoDto;
    }











}
