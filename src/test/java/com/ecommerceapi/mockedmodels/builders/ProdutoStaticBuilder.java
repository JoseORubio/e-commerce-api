package com.ecommerceapi.mockedmodels.builders;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.models.ProdutoModel;

import java.math.BigDecimal;

public class ProdutoStaticBuilder {
    private static ProdutoModel produtoModel = new ProdutoModel();
    private static ProdutoDTO produtoDto = new ProdutoDTO();

    public static ProdutoModel getProdutoModelSemId(){
        produtoModel.setId(null);
        produtoModel.setNome("Blusa Z");
        produtoModel.setQuantidade_estoque(10);
        produtoModel.setPreco(BigDecimal.valueOf(15.4));
        return produtoModel;
    }

    public static ProdutoDTO getProdutoDto() {
        produtoDto.setNome("Blusa Z");
        produtoDto.setPreco("15.4");
        produtoDto.setQuantidade_estoque("10");
        return produtoDto;
    }
    public static ProdutoDTO getProdutoDtoNomeInvalido() {
        produtoDto.setNome("&*+");
        produtoDto.setPreco("10");
        produtoDto.setQuantidade_estoque("10");
        return produtoDto;
    }
    public static ProdutoDTO getProdutoDtoPrecoInvalido() {
        produtoDto.setNome("Blusa Z");
        produtoDto.setPreco("a");
        produtoDto.setQuantidade_estoque("10");
        return produtoDto;
    }
    public static ProdutoDTO getProdutoDtoQuantidadeEstoqueInvalida() {
        produtoDto.setNome("Blusa Z");
        produtoDto.setPreco("15.4");
        produtoDto.setQuantidade_estoque("a");
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
