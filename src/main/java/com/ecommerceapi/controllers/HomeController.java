package com.ecommerceapi.controllers;

import com.ecommerceapi.models.ClienteModel;
import com.ecommerceapi.services.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
//@SessionAttributes("usuario")
public class HomeController {

    final ClienteService clienteService;

    public HomeController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public static ClienteModel clienteLogado;

    //    @RequestMapping(value="/login", method= RequestMethod.POST)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("login") String login,
                                        @RequestParam("senha") String senha) {
        if (!clienteService.autorizaCliente(login, senha)) {
            return ResponseEntity.status(HttpStatus.OK).body("Login ou senha incorretos.");
        }
        ClienteModel clienteModel = clienteService.buscarClientePorLogin(login).get();
        clienteLogado = clienteModel;
//        sessao.setAttribute("usuario", clienteModel);
        return ResponseEntity.status(HttpStatus.OK).body("Logado com sucesso.");
//        if (login.equals("as") && senha.equals("123")){
//            System.out.println("Deu certo!!!!!!!!!!!!");
//            ClienteModel clienteModel = new ClientesModel();
//            clientesModel.setId(109);
//            clientesModel.setNome("Vrunas");
//        sessao.setAttribute("usuario",login);

//            return "redirect:/usuario/autenticado";
//        else {
//            return "loginFalho";
//        }
    }

    @PostMapping("/logoff")
    public ResponseEntity<String> logoff( SessionStatus session) {
        if (clienteLogado == null){
            return ResponseEntity.status(HttpStatus.OK).body("Nenhum usuário logado");
        }
        clienteLogado = null;
//        session.setComplete();
        return ResponseEntity.status(HttpStatus.OK).body("Logoff com sucesso.");
    }
    @GetMapping("/clientelogado")
//    public ResponseEntity<String> verClienteLogado(@ModelAttribute("usuario") ClienteModel clienteModel) {
    public ResponseEntity<String> verClienteLogado() {
        if (clienteLogado == null){
            return ResponseEntity.status(HttpStatus.OK).body("Nenhum usuário logado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteLogado.getNome());
    }

}
