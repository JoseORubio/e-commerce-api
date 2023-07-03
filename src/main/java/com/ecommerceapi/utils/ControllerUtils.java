package com.ecommerceapi.utils;

import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ControllerUtils {

    @Autowired
    final UsuarioService usuarioService;

    public ControllerUtils(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

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
        }
    }

    public UsuarioModel pegarUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        String login = authentication.getName();
        UsuarioModel usuarioModel = usuarioService.buscarUsuarioPorLogin(login).get();
        return usuarioModel;
    }
}
