package com.ecommerceapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProdutoDTO {

    @NotBlank
    @Pattern(regexp = "[\\w\\u00C0-\\u00FF\\/\\\\#,.()\\-']+(\\s([\\w\\u00C0-\\u00FF\\/\\\\#(),.\\-'])+)*",
            message = "Pode conter qualquer letra, número e os seguintes caracteres: , . / # ( ) -")
    @Size(max = 80, message = "Nome deve ter no máximo {max} caracteres.")
    private String nome;

    @NotBlank
    @Pattern(regexp ="^\\d{1,6}$", message = "Deve ser qualquer número entre 0 e 999999.")
    private String quantidadeEstoque;

    @NotBlank
    @Pattern(regexp ="^\\d{1,6}([,|.]\\d{1,2})?$", message = "Deve ser qualquer valor monetário até 9999,99 com dois dígitos após , ou .")
    private String preco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(String quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
