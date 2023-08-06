package com.ecommerceapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

public class ManipuladorListaErros {

    private Map<String, StringBuilder> mapListaErros;

    public ManipuladorListaErros(BindingResult errosDeValidacao) {
        this.mapListaErros = new HashMap<>();
        if (errosDeValidacao.hasErrors()) {
            List<FieldError> listaFieldErros = errosDeValidacao.getFieldErrors();
            for (FieldError erro : listaFieldErros) {
                adicionarErros(erro.getField(), erro.getDefaultMessage());
            }
        }
    }

//    public ManipuladorListaErros(List<List<String>> listaErros) {
//        this.listaErros = listaErros;
//    }

    //Parece que não será usado
//    public List<Map<String, StringBuilder>> getListaErros() {
//        return listaErrosString;
//    }

    public boolean temErros() {
        return !mapListaErros.isEmpty();
    }


    public void adicionarErros(String campoErro, String msgErro) {
        if (!mapListaErros.containsKey(campoErro)) {
            mapListaErros.put(campoErro, new StringBuilder(formataDescricaoErro(msgErro)));
        } else {
            StringBuilder erros = mapListaErros.get(campoErro);
            erros.append(formataDescricaoErro(msgErro));
            mapListaErros.put(campoErro, erros);
        }
    }

    private String formataDescricaoErro(String msgErro) {
        return String.valueOf(" "
                + msgErro.toUpperCase().charAt(0)
                + msgErro.substring(1)
                + (msgErro.charAt(msgErro.length() - 1) != '.' ? "." : ""));
    }


    public String converteListaErrosParaStringJson() {

        StringBuilder stringJSON = new StringBuilder();
        Set<String> campos = mapListaErros.keySet();

        stringJSON.append("[");
        for (String campo : campos) {
            stringJSON
                    .append("{")
                    .append("\"Campo\"" + ":" + "\"" + campo + "\",")
                    .append("\"Erros\"" + ":" + "\"" + mapListaErros.get(campo).substring(1) + "\"},");
        }
        stringJSON.deleteCharAt(stringJSON.length() - 1);
        stringJSON.append("]");
        return stringJSON.toString();
    }

    public static JsonNode converteStringJsonParaJsonNode(String stringJson) {

        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree(stringJson);
        } catch (JsonProcessingException exception) {
        }
        return jsonNode;
    }

    public static Map<String ,String > mapErroUnitario(String campoErro, String msgErro ){
        Map<String ,String > map = new HashMap<>(1);
        map.put(campoErro,msgErro);
        return map;

    }
}
