package com.ecommerceapi.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;

public class ClienteModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotEmpty @Size(max=20,message="Nome deve ter no máximo {max} caracteres ")
    private String nome;
    @NotEmpty @Size(min = 8, max=20,message="Login deve ter entre {min} e {max} caracteres ")
    private String login;
    @NotEmpty @Size(min = 8, max=20,message="Senha deve ter entre {min} e {max} caracteres ")
    private String senha;
    @NotEmpty @Pattern(regexp = "\\d{5}-\\d{3}", message = "Siga o padrão 12345-678")
    private String  cep;
    private String uf;
    private String cidade;
    private String rua;
    @NotNull @Range(min = 1, max = 99999)
    private int numero_rua;


}
