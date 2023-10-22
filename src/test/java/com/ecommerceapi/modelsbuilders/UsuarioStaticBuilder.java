package com.ecommerceapi.modelsbuilders;

import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.dtos.UsuarioViewDTO;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.utils.ConversorUUID;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuarioStaticBuilder {

    private static UsuarioModel usuarioModel = new UsuarioModel();
    private static UsuarioModel usuarioModelMock = new UsuarioModel();
    private static UsuarioDTO usuarioDTO = new UsuarioDTO();
    private static UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO();

    private static String nome = "Tiago da Silva";
    private static String login = "tiago123";
    private static String senha = "ASd123Asd";
    private static String cpf = "63224899093";
    private static LocalDate dataNasc = LocalDate.of(1980, 05, 15);
    private static String dataNascString = "15/05/1980";
    private static LocalDateTime dataCadastro = LocalDateTime.now();
    private static String sexo = "M";
    private static String telefone = "(61)91236-1932";
    private static String email = "tiagosilva@gmail.com";
    private static String cep = "53240-685";
    private static String uf = "PE";
    private static String cidade = "Olinda";
    private static String rua = "Travessa São João";
    private static String numeroRua = "651";

    public static UsuarioModel getUsuarioModelSemId() {
        usuarioModel.setId(null);
        usuarioModel.setNome(nome);
        usuarioModel.setLogin(login);
        usuarioModel.setSenha(senha);
        usuarioModel.setCpf(cpf);
        usuarioModel.setData_nasc(dataNasc);
        usuarioModel.setData_cadastro(dataCadastro);
        usuarioModel.setSexo(sexo.charAt(0));
        usuarioModel.setTelefone(telefone);
        usuarioModel.setEmail(email);
        usuarioModel.setCep(cep);
        usuarioModel.setUf(uf);
        usuarioModel.setCidade(cidade);
        usuarioModel.setRua(rua);
        usuarioModel.setNumero_rua(Integer.parseInt(numeroRua));

        return usuarioModel;
    }

    public static UsuarioModel getMockUsuarioModelComId() {
        usuarioModelMock.setId(ConversorUUID.converteUUID("b77a0ee4-3ec2-479b-9640-a215a6cab4a3"));
        usuarioModelMock.setNome(nome);
        usuarioModelMock.setLogin(login);
        usuarioModelMock.setSenha(senha);
        usuarioModelMock.setCpf(cpf);
        usuarioModelMock.setData_nasc(dataNasc);
        usuarioModelMock.setData_cadastro(dataCadastro);
        usuarioModelMock.setSexo(sexo.charAt(0));
        usuarioModelMock.setTelefone(telefone);
        usuarioModelMock.setEmail(email);
        usuarioModelMock.setCep(cep);
        usuarioModelMock.setUf(uf);
        usuarioModelMock.setCidade(cidade);
        usuarioModelMock.setRua(rua);
        usuarioModelMock.setNumero_rua(Integer.parseInt(numeroRua));

        return usuarioModelMock;
    }

    public static UsuarioDTO getUsuarioDTO() {
        usuarioDTO.setNome(nome);
        usuarioDTO.setLogin(login);
        usuarioDTO.setSenha(senha);
        usuarioDTO.setCpf(cpf);
        usuarioDTO.setData_nasc(dataNascString);
        usuarioDTO.setSexo(sexo);
        usuarioDTO.setTelefone(telefone);
        usuarioDTO.setEmail(email);
        usuarioDTO.setCep(cep);
        usuarioDTO.setNumero_rua(numeroRua);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTO2() {
        usuarioDTO.setNome("Nicole Teresinha Lopes");
        usuarioDTO.setLogin("nicole123");
        usuarioDTO.setSenha("TYu123Asd");
        usuarioDTO.setCpf("15876511498");
        usuarioDTO.setData_nasc("22/02/1922");
        usuarioDTO.setSexo("F");
        usuarioDTO.setTelefone("(85)99951-1672");
        usuarioDTO.setEmail("nicole_lopes@netservicos.com.br");
        usuarioDTO.setCep("60422-670");
        usuarioDTO.setNumero_rua("912");

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTONomeInvalido() {
        usuarioDTO.setNome("******");
        usuarioDTO.setLogin(login);
        usuarioDTO.setSenha(senha);
        usuarioDTO.setCpf(cpf);
        usuarioDTO.setData_nasc(dataNascString);
        usuarioDTO.setSexo(sexo);
        usuarioDTO.setTelefone(telefone);
        usuarioDTO.setEmail(email);
        usuarioDTO.setCep(cep);
        usuarioDTO.setNumero_rua(numeroRua);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTOLoginInvalido() {
        usuarioDTO.setNome(nome);
        usuarioDTO.setLogin("******");
        usuarioDTO.setSenha(senha);
        usuarioDTO.setCpf(cpf);
        usuarioDTO.setData_nasc(dataNascString);
        usuarioDTO.setSexo(sexo);
        usuarioDTO.setTelefone(telefone);
        usuarioDTO.setEmail(email);
        usuarioDTO.setCep(cep);
        usuarioDTO.setNumero_rua(numeroRua);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTOSenhaInvalida() {
        usuarioDTO.setNome(nome);
        usuarioDTO.setLogin(login);
        usuarioDTO.setSenha("******");
        usuarioDTO.setCpf(cpf);
        usuarioDTO.setData_nasc(dataNascString);
        usuarioDTO.setSexo(sexo);
        usuarioDTO.setTelefone(telefone);
        usuarioDTO.setEmail(email);
        usuarioDTO.setCep(cep);
        usuarioDTO.setNumero_rua(numeroRua);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTOCPFInvalido() {
        usuarioDTO.setNome(nome);
        usuarioDTO.setLogin(login);
        usuarioDTO.setSenha(senha);
        usuarioDTO.setCpf("******");
        usuarioDTO.setData_nasc(dataNascString);
        usuarioDTO.setSexo(sexo);
        usuarioDTO.setTelefone(telefone);
        usuarioDTO.setEmail(email);
        usuarioDTO.setCep(cep);
        usuarioDTO.setNumero_rua(numeroRua);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTODataNascInvalida() {
        usuarioDTO.setNome(nome);
        usuarioDTO.setLogin(login);
        usuarioDTO.setSenha(senha);
        usuarioDTO.setCpf(cpf);
        usuarioDTO.setData_nasc("******");
        usuarioDTO.setSexo(sexo);
        usuarioDTO.setTelefone(telefone);
        usuarioDTO.setEmail(email);
        usuarioDTO.setCep(cep);
        usuarioDTO.setNumero_rua(numeroRua);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTOSexoInvalido() {
        usuarioDTO.setNome(nome);
        usuarioDTO.setLogin(login);
        usuarioDTO.setSenha(senha);
        usuarioDTO.setCpf(cpf);
        usuarioDTO.setData_nasc(dataNascString);
        usuarioDTO.setSexo("******");
        usuarioDTO.setTelefone(telefone);
        usuarioDTO.setEmail(email);
        usuarioDTO.setCep(cep);
        usuarioDTO.setNumero_rua(numeroRua);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTOTelefoneInvalido() {
        usuarioDTO.setNome(nome);
        usuarioDTO.setLogin(login);
        usuarioDTO.setSenha(senha);
        usuarioDTO.setCpf(cpf);
        usuarioDTO.setData_nasc(dataNascString);
        usuarioDTO.setSexo(sexo);
        usuarioDTO.setTelefone("******");
        usuarioDTO.setEmail(email);
        usuarioDTO.setCep(cep);
        usuarioDTO.setNumero_rua(numeroRua);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTOEmailInvalido() {
        usuarioDTO.setNome(nome);
        usuarioDTO.setLogin(login);
        usuarioDTO.setSenha(senha);
        usuarioDTO.setCpf(cpf);
        usuarioDTO.setData_nasc(dataNascString);
        usuarioDTO.setSexo(sexo);
        usuarioDTO.setTelefone(telefone);
        usuarioDTO.setEmail("******");
        usuarioDTO.setCep(cep);
        usuarioDTO.setNumero_rua(numeroRua);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTOCEPInvalido() {
        usuarioDTO.setNome(nome);
        usuarioDTO.setLogin(login);
        usuarioDTO.setSenha(senha);
        usuarioDTO.setCpf(cpf);
        usuarioDTO.setData_nasc(dataNascString);
        usuarioDTO.setSexo(sexo);
        usuarioDTO.setTelefone(telefone);
        usuarioDTO.setEmail(email);
        usuarioDTO.setCep("******");
        usuarioDTO.setNumero_rua(numeroRua);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTONumeroRuaInvalido() {
        usuarioDTO.setNome(nome);
        usuarioDTO.setLogin(login);
        usuarioDTO.setSenha(senha);
        usuarioDTO.setCpf(cpf);
        usuarioDTO.setData_nasc(dataNascString);
        usuarioDTO.setSexo(sexo);
        usuarioDTO.setTelefone(telefone);
        usuarioDTO.setEmail(email);
        usuarioDTO.setCep(cep);
        usuarioDTO.setNumero_rua("******");

        return usuarioDTO;
    }


    public static UsuarioDTO getUsuarioDTOAtributosNulos() {
        usuarioDTO.setNome(null);
        usuarioDTO.setLogin(null);
        usuarioDTO.setSenha(null);
        usuarioDTO.setCpf(null);
        usuarioDTO.setData_nasc(null);
        usuarioDTO.setSexo(null);
        usuarioDTO.setTelefone(null);
        usuarioDTO.setEmail(null);
        usuarioDTO.setCep(null);
        usuarioDTO.setNumero_rua(null);

        return usuarioDTO;
    }

    public static UsuarioDTO getUsuarioDTOStringsVazias() {
        usuarioDTO.setNome("");
        usuarioDTO.setLogin("");
        usuarioDTO.setSenha("");
        usuarioDTO.setCpf("");
        usuarioDTO.setData_nasc("");
        usuarioDTO.setSexo("");
        usuarioDTO.setTelefone("");
        usuarioDTO.setEmail("");
        usuarioDTO.setCep("");
        usuarioDTO.setNumero_rua("");

        return usuarioDTO;
    }

    public static UsuarioViewDTO getUsuarioViewDTO() {
        usuarioViewDTO.setNome(nome);
        usuarioViewDTO.setLogin(login);
        usuarioViewDTO.setCpf(cpf);
        usuarioViewDTO.setData_nasc(dataNasc);
        usuarioViewDTO.setSexo(sexo.charAt(0));
        usuarioViewDTO.setTelefone(telefone);
        usuarioViewDTO.setEmail(email);
        usuarioViewDTO.setCep(cep);
        usuarioViewDTO.setUf(uf);
        usuarioViewDTO.setCidade(cidade);
        usuarioViewDTO.setRua(rua);
        usuarioViewDTO.setNumero_rua(Integer.parseInt(numeroRua));

        return usuarioViewDTO;
    }
}
