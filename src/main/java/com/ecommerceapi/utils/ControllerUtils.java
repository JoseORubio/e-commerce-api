package com.ecommerceapi.utils;

import java.util.HashMap;
import java.util.Map;

public class ControllerUtils {

    public static  Map<String, String> adicionarErros(String campoErro, String msgErro) {
        Map<String, String> mapErro = new HashMap<String, String>();
        mapErro.put("campo", campoErro);
        mapErro.put("mensagem", msgErro);
        return mapErro;
    }
}
