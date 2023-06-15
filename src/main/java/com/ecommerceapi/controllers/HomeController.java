package com.ecommerceapi.controllers;

import com.ecommerceapi.models.ClienteModel;
import com.ecommerceapi.services.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class HomeController {

    @Autowired
    ClienteService clienteService;
    @Autowired
    CarrinhoController carrinhoController;

    public static ClienteModel clienteLogado;
    @PostMapping
    public ResponseEntity<String> login(@RequestParam("login") String login,
                                        @RequestParam("senha") String senha) {

        if (!(clienteLogado == null)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já logado.");
        }

        if (!clienteService.autorizaCliente(login, senha)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login ou senha incorretos.");
        }

        ClienteModel clienteModel = clienteService.buscarClientePorLogin(login).get();
        clienteLogado = clienteModel;
        return ResponseEntity.status(HttpStatus.OK).body("Logado com sucesso.");
    }

    @DeleteMapping
    public ResponseEntity<String>  logoff() {
        if (clienteLogado == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum usuário logado");
        }
        carrinhoController.cancelaCarrinho();
        clienteLogado = null;
        return ResponseEntity.status(HttpStatus.OK).body("Logoff com sucesso.");
    }
    @GetMapping
    public ResponseEntity<String> verClienteLogado() {
        if (clienteLogado == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário logado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteLogado.getNome());
    }

}
