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
    public static ProdutoDTO getProdutoDtoPrecoInvalido() {
            produtoDto.setNome("Blusa Z");
            produtoDto.setPreco("a");
            produtoDto.setQuantidade_estoque("10");
        return produtoDto;
    }

}
