package com.ecommerceapi.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

//@Entity @Table(name = "pdv_teste")
@Entity @Table(name = "produtos_da_venda")
public class ProdutoDaVendaModel implements Serializable {
    private final static long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id_item_venda;
    private UUID id_venda;
    private UUID id_produto;
    private int quantidade;
    private BigDecimal valor_total_produto;

//    @ManyToOne
//    @JoinColumn(name = "id_venda",referencedColumnName = "id" , updatable = false)
//    private VendaModel venda;
//    @ManyToOne
//    @JoinColumn(name = "id_produto",referencedColumnName = "id",  updatable = false)
//    private ProdutoModel produto;
//
//    public VendaModel getVenda() {
//        return venda;
//    }
//
//    public void setVenda(VendaModel venda) {
//        this.venda = venda;
//    }
//
//    public ProdutoModel getProduto() {
//        return produto;
//    }
//
//    public void setProduto(ProdutoModel produto) {
//        this.produto = produto;
//    }

    public UUID getId_item_venda() {
        return id_item_venda;
    }

    public void setId_item_venda(UUID id_item_venda) {
        this.id_item_venda = id_item_venda;
    }

    public UUID getId_venda() {
        return id_venda;
    }

    public void setId_venda(UUID id_venda) {
        this.id_venda = id_venda;
    }

    public UUID getId_produto() {
        return id_produto;
    }

    public void setId_produto(UUID id_produto) {
        this.id_produto = id_produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor_total_produto() {
        return valor_total_produto;
    }

    public void setValor_total_produto(BigDecimal valor_total_produto) {
        this.valor_total_produto = valor_total_produto;
    }
}
