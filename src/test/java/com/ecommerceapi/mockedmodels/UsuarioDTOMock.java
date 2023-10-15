package com.ecommerceapi.mockedmodels;

import com.ecommerceapi.dtos.UsuarioDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuarioDTOMock extends UsuarioDTO {
    public UsuarioDTOMock() {
        this.setNome("Tiago da Silva");
        this.setLogin("tiago123");
        this.setSenha("ASd123Asd");
        this.setCpf("63224899093");
        this.setData_nasc("15/05/1980");
        this.setSexo("M");
        this.setTelefone("(61)91236-1932");
        this.setEmail("tiagosilva@gmail.com");
        this.setCep("53240-685");
        this.setNumero_rua("421");
    }
}
