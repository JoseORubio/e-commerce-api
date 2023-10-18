package com.ecommerceapi.mockedmodels;

import com.ecommerceapi.dtos.ProdutoDTO;

public class ProdutoDTOStaticBuilder {
    private static ProdutoDTO produtoDto = new ProdutoDTO();

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
