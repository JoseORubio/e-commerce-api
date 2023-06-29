package com.ecommerceapi.models;

import com.ecommerceapi.enums.RoleName;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "papeis")
public class PapelModel implements GrantedAuthority, Serializable {
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private RoleName nome;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public RoleName getNome() {
        return nome;
    }

    public void setNome(RoleName nome) {
        this.nome = nome;
    }

    @Override
    public String getAuthority() {
        return this.nome.toString();
    }
}
