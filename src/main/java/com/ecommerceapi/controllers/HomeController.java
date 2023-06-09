package com.ecommerceapi.controllers;

import com.ecommerceapi.models.ClienteModel;
import com.ecommerceapi.services.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    final ClienteService clienteService;

    public HomeController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public static ClienteModel clienteLogado;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("login") String login,
                                        @RequestParam("senha") String senha) {
        if (!clienteService.autorizaCliente(login, senha)) {
            return ResponseEntity.status(HttpStatus.OK).body("Login ou senha incorretos.");
        }

        if (!(clienteLogado == null)){
            return ResponseEntity.status(HttpStatus.OK).body("Usuário já logado.");
        }

        ClienteModel clienteModel = clienteService.buscarClientePorLogin(login).get();
        clienteLogado = clienteModel;
        return ResponseEntity.status(HttpStatus.OK).body("Logado com sucesso.");
    }

    @PostMapping("/logoff")
    public ResponseEntity<String>  logoff() {
        if (clienteLogado == null){
            return ResponseEntity.status(HttpStatus.OK).body("Nenhum usuário logado");
        }

        clienteLogado = null;
        return ResponseEntity.status(HttpStatus.OK).body("Logoff com sucesso.");
    }
    @GetMapping("/clientelogado")
    public ResponseEntity<String> verClienteLogado() {
        if (clienteLogado == null){
            return ResponseEntity.status(HttpStatus.OK).body("Nenhum usuário logado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteLogado.getNome());
    }

}
