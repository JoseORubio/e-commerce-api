package com.ecommerceapi.mockedmodels;

import com.ecommerceapi.dtos.UsuarioViewDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuarioViewDTOMock extends UsuarioViewDTO {

    public UsuarioViewDTOMock() {
        this.setNome("Tiago da Silva");
        this.setLogin("tiago123");
        this.setCpf("63224899093");
        this.setData_nasc(LocalDate.of(1980,05,15));
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
