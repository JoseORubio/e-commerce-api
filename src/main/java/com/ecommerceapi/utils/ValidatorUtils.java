package com.ecommerceapi.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ValidatorUtils {

    private List<List<String>> listaErros;

    public ValidatorUtils(BindingResult errosDeValidacao) {
        this.listaErros = new ArrayList<>();
        if (errosDeValidacao.hasErrors()) {
            List<FieldError> listaFieldErros = errosDeValidacao.getFieldErrors();
            for (FieldError erro : listaFieldErros) {
                adicionarErros(erro.getField(), erro.getDefaultMessage());
            }
        }
    }

    public ValidatorUtils(List<List<String>> listaErros) {
        this.listaErros = listaErros;
    }

    public List<List<String>> getListaErros() {
        return listaErros;
    }

    public void adicionarErros(String campoErro, String msgErro) {
        boolean campoRepetido = false;
        for (List<String> erro : listaErros) {
            if (erro.get(0).equals(campoErro)) {
                campoRepetido = true;
                erro.add(msgErro);
            }
        }
        if (!campoRepetido) {
            List<String> camposEMensagens = new ArrayList<>();
            camposEMensagens.add(campoErro);
            camposEMensagens.add(msgErro);
            listaErros.add(camposEMensagens);
        }
    }

    public static List<Map<String, String>> converteListaErrosParaMap(List<List<String>> listaErros) {

        List<Map<String, String>> listaErrosMap = new ArrayList<>();

        for (List<String> erro : listaErros) {
            Map<String, String> infoItens = new LinkedHashMap<>();
            boolean chaveCampo = true;
            String mensagensErro = "";
            for (String valoresErro : erro) {
                if (chaveCampo) {
                    infoItens.put("Campo", valoresErro);
                    chaveCampo = false;
                } else {
                    mensagensErro = String.format(mensagensErro
                            + valoresErro.toUpperCase().charAt(0)
                            + valoresErro.substring(1)
                            + (valoresErro.charAt(valoresErro.length() - 1) != '.' ? "." : "")
                            + " ");
                }
            }
            infoItens.put("Erros", mensagensErro.substring(0, mensagensErro.length() - 1));
            listaErrosMap.add(infoItens);
        }
        return listaErrosMap;
    }
}
