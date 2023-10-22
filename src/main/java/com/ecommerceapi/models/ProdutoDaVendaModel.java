package com.ecommerceapi.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity @Table(name = "produtos_da_venda")
public class ProdutoDaVendaModel implements Serializable {
    private final static long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_item_venda")
    private UUID idItemVenda;
    @Column(name = "id_venda")
    private UUID idVenda;
    @Column(name = "id_produto")
    private UUID idProduto;
    private int quantidade;
    @Column(name = "valor_total_produto")
    private BigDecimal valorTotalProduto;

    public ProdutoDaVendaModel(){}
    public ProdutoDaVendaModel(UUID idVenda, UUID idProduto, int quantidade) {
        this.idVenda = idVenda;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public UUID getIdItemVenda() {
        return idItemVenda;
    }

    public void setIdItemVenda(UUID idItemVenda) {
        this.idItemVenda = idItemVenda;
    }

    public UUID getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(UUID idVenda) {
        this.idVenda = idVenda;
    }

    public UUID getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(UUID idProduto) {
        this.idProduto = idProduto;
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
