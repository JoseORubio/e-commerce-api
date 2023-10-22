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
    @Column(name = "id_usuario")
    private UUID idUsuario;
    @Column(name = "id_papel")
    private UUID idPapel;

    public PapelDoUsuarioModel(){}
    public PapelDoUsuarioModel(UUID idUsuario, UUID idPapel) {
        this.idUsuario = idUsuario;
        this.idPapel = idPapel;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(UUID idUsuario) {
        this.idUsuario = idUsuario;
    }

    public UUID getIdPapel() {
        return idPapel;
    }

    public void setIdPapel(UUID idPapel) {
        this.idPapel = idPapel;
    }
}
