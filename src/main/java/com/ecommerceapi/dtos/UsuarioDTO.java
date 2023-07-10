package com.ecommerceapi.dtos;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public class UsuarioDTO {

    @NotBlank
    @Size(max = 80, message = "Nome deve ter no máximo {max} caracteres.")
    @Pattern(regexp = "[A-Z\\u00C0-\\u00FF][a-zA-Z\\u00C0-\\u00FF]+(\\s([a-zA-Z\\u00C0-\\u00FF])+)*",
            message = "Deve conter apenas letras e obedeçer o padrão 'Ana da Silva Pereira' com ao menos a primeira letra maiúscula.")
    private String nome;

    @NotBlank
    @Size(min = 8, max = 20, message = "Login deve ter entre {min} e {max} caracteres.")
    @Pattern(regexp = "^(?=.*[a-z])[a-z\\d_]+$", message = "Deve conter apenas números ou '_', e pelo menos uma letra minúscula.")
    private String login;

    @NotBlank
    @Size(min = 8, max = 20, message = "Senha deve ter entre {min} e {max} caracteres.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,20}$",
            message = "Deve conter ao menos uma letra maiúscula, uma minúscula e um número.")
    private String senha;

    @NotBlank
    @CPF(message = "Número do registro de contribuinte individual brasileiro (CPF) inválido.")
    private String cpf;

    @NotBlank
    @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Deve estar no formato: 'dd/MM/yyyy'.")
    private String data_nasc;

    @NotBlank
    @Pattern(regexp = "^(?:M|F|O)$", message = "Apenas 'M' para masculino, 'F' para feminino e 'O' para outro.")
    private String sexo;

    @NotBlank
    @Pattern(regexp = "^\\(\\d{2}\\)\\d{5}\\-\\d{4}$", message = "Deve seguir o padrão, contendo DDD: (12)12345-1234.")
    private String telefone;

    @NotBlank
    @Email(message = "E-mail inválido.")
    @Size(max = 50, message = "E-mail deve ter no máximo {max} caracteres.")
    private String email;

    @NotEmpty
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Siga o padrão 12345-678.")
    private String cep;

    @NotBlank
    @Pattern(regexp = "^[1-9]\\d{0,4}$", message = "Deve ser qualquer número entre 1 e 99999.")
    private String numero_rua;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public String getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(String data_nasc) {
        this.data_nasc = data_nasc;
    }


    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }


    public String getNumero_rua() {
        return numero_rua;
    }

    public void setNumero_rua(String numero_rua) {
        this.numero_rua = numero_rua;
    }
}
