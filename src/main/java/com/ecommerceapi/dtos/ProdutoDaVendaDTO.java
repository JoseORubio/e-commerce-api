package com.ecommerceapi.dtos;


import jakarta.validation.constraints.NotBlank;

public class ProdutoDaVendaDTO {


    @NotBlank
    private String id_produto;
    @NotBlank
    private String quantidade;
    @NotBlank
    private String valor_total_produto;
}
