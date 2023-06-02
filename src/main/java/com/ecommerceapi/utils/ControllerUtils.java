package com.ecommerceapi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ControllerUtils {

    public static  Map<String, String> adicionarErros(String campoErro, String msgErro) {
        Map<String, String> mapErro = new HashMap<String, String>();
        mapErro.put("campo", campoErro);
        mapErro.put("mensagem", msgErro);
        return mapErro;
    }
    public static UUID converteUUID(String id) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
            return uuid;
        } catch (Exception e) {
            return null;
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
    }
}
