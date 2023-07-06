package com.ecommerceapi.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "papeis_do_usuario")
public class PapelDoUsuarioModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID id_usuario;
    private UUID id_papel;

    public PapelDoUsuarioModel(){}
    public PapelDoUsuarioModel(UUID id_usuario, UUID id_papel) {
        this.id_usuario = id_usuario;
        this.id_papel = id_papel;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(UUID id_usuario) {
        this.id_usuario = id_usuario;
    }

    public UUID getId_papel() {
        return id_papel;
    }

    public void setId_papel(UUID id_papel) {
        this.id_papel = id_papel;
    }
}
