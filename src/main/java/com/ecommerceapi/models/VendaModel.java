package com.ecommerceapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vendas")
public class VendaModel implements Serializable {
    private final static long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private BigDecimal valor_total;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd/MM/yyyy 'às' HH:mm:ss 'UTC'")
    private LocalDateTime data_venda;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getValor_total() {
        return valor_total;
    }

    public void setValor_total(BigDecimal valor_total) {
        this.valor_total = valor_total;
    }

    public LocalDateTime getData_venda() {
        return data_venda;
    }

    public void setData_venda(LocalDateTime data_venda) {
        this.data_venda = data_venda;
    }
}
