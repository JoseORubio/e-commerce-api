//package com.ecommerceapi.controllers;
//
//import com.ecommerceapi.models.UsuarioModel;
//import com.ecommerceapi.services.UsuarioService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/login")
//public class HomeController {
//
//    @Autowired
//    UsuarioService usuarioService;
//    @Autowired
//    CarrinhoController carrinhoController;
//
//    public static UsuarioModel clienteLogado;
//    @PostMapping
//    public ResponseEntity<String> login(@RequestParam("login") String login,
//                                        @RequestParam("senha") String senha) {
//
//        if (!(clienteLogado == null)){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já logado.");
//        }
//
//        if (!usuarioService.autorizaCliente(login, senha)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login ou senha incorretos.");
//        }
//
//        UsuarioModel usuarioModel = usuarioService.buscarClientePorLogin(login).get();
//        clienteLogado = usuarioModel;
//        return ResponseEntity.status(HttpStatus.OK).body("Logado com sucesso.");
//    }
//
//    @DeleteMapping
//    public ResponseEntity<String>  logoff() {
//        if (clienteLogado == null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum usuário logado");
//        }
//        carrinhoController.cancelaCarrinho();
//        clienteLogado = null;
//        return ResponseEntity.status(HttpStatus.OK).body("Logoff com sucesso.");
//    }
//    @GetMapping
//    public ResponseEntity<String> verClienteLogado() {
//        if (clienteLogado == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário logado");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(clienteLogado.getNome());
//    }
//
//}
