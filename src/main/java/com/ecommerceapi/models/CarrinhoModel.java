package com.ecommerceapi.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "carrinho")
public class CarrinhoModel implements Serializable {
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    private UsuarioModel usuario;
    @ManyToOne
    private ProdutoModel produto;
    private int quantidade;
    private BigDecimal valorTotalProduto;

    public CarrinhoModel(UsuarioModel usuario, ProdutoModel produto, int quantidade) {
        this.usuario = usuario;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorTotalProduto = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }
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
        this.valorTotalProduto = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
    }

    public BigDecimal getValorTotalProduto() {
        return valorTotalProduto;
    }

    public void setValorTotalProduto(BigDecimal valorTotalProduto) {
        this.valorTotalProduto = valorTotalProduto;
    }
}
