package com.ecommerceapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.websocket.OnMessage;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.UniqueElements;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clientes-teste")
public class ClienteModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotEmpty
    @Size(max = 80, message = "Nome deve ter no máximo {max} caracteres ")
    @Pattern(regexp = "[A-Z][a-zA-Z\\u00C0-\\u00FF]+(\\s([a-zA-Z\\u00C0-\\u00FF])+)*", message = "Deve obedeçer o padrão 'Ana da Silva Pereira' com ao menos a primeira letra maiúscula")
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

    @NotEmpty
    @Size(min = 8, max = 20, message = "Login deve ter entre {min} e {max} caracteres ")
    @Pattern(regexp = "^(?=.*[a-z])[a-z\\d_]+$", message = "Deve conter apenas números ou '_', e pelo menos uma letra minúscula ")
    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
//
    @NotEmpty @Size(min = 8, max=20,message="Senha deve ter entre {min} e {max} caracteres ")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$",
             message = "Deve conter ao menos uma letra maiúscula, uma minúscula e um número")
    private String senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
//
    @NotEmpty @CPF(message = "Número do registro de contribuinte individual brasileiro (CPF) inválido. Digite apenas números.")
    private String cpf;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    @NotNull
//    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @JsonFormat (pattern = "dd-MM-yyyy")

    private LocalDate data_nasc;

    public LocalDate getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(LocalDate data_nasc) {
        this.data_nasc = data_nasc;
    }

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
