package com.ecommerceapi.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ConversorUUID {

    public static  Map<String, String> adicionarErros(String campoErro, String msgErro) {
//        Map<String, String> mapErro = new HashMap<String, String>();
        Map<String, String> mapErro =  new LinkedHashMap<>();
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
        }
    }

}
