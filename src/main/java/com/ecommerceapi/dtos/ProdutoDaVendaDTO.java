package com.ecommerceapi.dtos;


import jakarta.validation.constraints.NotBlank;

public class ProdutoDaVendaDTO {


    @NotBlank
    private String idProduto;
    @NotBlank
    private String quantidade;
    @NotBlank
    private String valorTotalProduto;
}
