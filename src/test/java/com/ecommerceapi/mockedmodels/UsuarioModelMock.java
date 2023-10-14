package com.ecommerceapi.mockedmodels;

import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.utils.ConversorUUID;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class UsuarioModelMock extends UsuarioModel {

    public UsuarioModelMock() {
        this.setId( ConversorUUID.converteUUID("b77a0ee4-3ec2-479b-9640-a215a6cab4a3"));
        this.setNome("Tiago da Silva");
        this.setLogin("tiago123");
        this.setSenha("ASd123Asd");
        this.setCpf("63224899093");
        this.setData_nasc(LocalDate.of(1980,05,15));
        this.setData_cadastro(LocalDateTime.now());
        this.setSexo('M');
        this.setTelefone("(61)91236-1932");
        this.setEmail("tiagosilva@gmail.com");
        this.setCep("53240-685");
        this.setUf("PE");
        this.setCidade("Olinda");
        this.setRua("Travessa São João");
        this.setNumero_rua(651);
    }
}
