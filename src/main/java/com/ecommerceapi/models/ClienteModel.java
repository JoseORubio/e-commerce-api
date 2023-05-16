package com.ecommerceapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table (name = "clientes-teste")
public class ClienteModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty @Size(max=80,message="Nome deve ter no máximo {max} caracteres ")
    @Pattern (regexp = "[A-Z][a-zA-Z\\u00C0-\\u00FF]+(\\s([a-zA-Z\\u00C0-\\u00FF])+)*",
            message = "Deve obedeçer o padrão 'Ana da Silva Pereira' com ao menos a primeira letra maiúscula")
    private String nome;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
//    @NotEmpty @Size(min = 8, max=20,message="Login deve ter entre {min} e {max} caracteres ")
//    @Pattern(regexp = "^[a-z\\d_]+$", message = "Deve conter apenas letras minúsculas, números ou '_'")
//    private String login;
//
//    @NotEmpty @Size(min = 8, max=20,message="Senha deve ter entre {min} e {max} caracteres ")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$",
//             message = "Deve conter ao menos uma letra maiúscula, uma minúscula e um número")
//    private String senha;
//
//    @NotEmpty @CPF
//    private String cpf;
//
//    @NotEmpty
//    private LocalDate data_nasc;
//    @NotEmpty
//    private LocalDateTime data_cadastro;
//
//    @NotEmpty @Pattern(regexp = "^(?:M|F|O)$", message = "Apenas 'M' para masculino, 'F' para feminino e 'O' para outro")
//    private char sexo;
//
//    @NotEmpty @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}\\-\\d{4}$", message = "Padrão (12)12345-1234")
//    private String telefone;
//
//    @NotEmpty @Email @Size( max=40,message="E-mail deve ter no máximo {max} caracteres ")
//    private String email;
//
//
//    @NotEmpty @Pattern(regexp = "\\d{5}-\\d{3}", message = "Siga o padrão 12345-678")
//    private String  cep;
//    private String uf;
//    private String cidade;
//    private String rua;
//
//    @NotNull @Range(min = 1, max = 99999)
//    private int numero_rua;


}
