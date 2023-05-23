package com.ecommerceapi.utils;

import com.ecommerceapi.models.ClienteModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CEPUtils {

    private String converteCEP(String cep) {
        String[] tokens = cep.split("-");
        return tokens[0] + tokens[1];
    }

    private String buscaDados(String cep) {
        try {
//            String url = "https://viacep.com.br/ws/17060480/json/";
//          String url = "https://viacep.com.br/ws/12345678/json/";
//          String url = "https://viacep.com.br/ws/123123bd/json/";
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            URI endereco = URI.create(url);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(endereco).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            if (response.statusCode() == 400) {
//                throw new RuntimeException("Pesquisa inválida");
//            }

//          System.out.println(response.statusCode());
            String body = response.body();
            return body;
        } catch (IOException | InterruptedException e) {
//          e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ClienteModel retornaCep(ClienteModel cliente) {

        String dados = buscaDados(converteCEP(cliente.getCep()));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(dados);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (node.get("erro") != null) {
            throw new RuntimeException();
//            throw new RuntimeException("CEP não encontrado");
//            System.out.println("CEP não encontrado");
        }
        cliente.setRua(node.get("logradouro").asText());
        cliente.setCidade(node.get("localidade").asText());
        cliente.setUf(node.get("uf").asText());
        return cliente;

    }

}
