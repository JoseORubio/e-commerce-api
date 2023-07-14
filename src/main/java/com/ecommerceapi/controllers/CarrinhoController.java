package com.ecommerceapi.controllers;

import com.ecommerceapi.models.*;
import com.ecommerceapi.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;



@RestController
@RequestMapping("/itens-carrinho")
public class CarrinhoController {

    final VendaService vendaService;
    final ProdutoDaVendaService produtoDaVendaService;
    final ProdutoService produtoService;
    final UsuarioService usuarioService;
    final CarrinhoService carrinhoService;


    public CarrinhoController(VendaService vendaService, ProdutoDaVendaService produtoDaVendaService, ProdutoService produtoService, UsuarioService usuarioService, CarrinhoService carrinhoService) {
        this.vendaService = vendaService;
        this.produtoDaVendaService = produtoDaVendaService;
        this.produtoService = produtoService;
        this.usuarioService = usuarioService;
        this.carrinhoService = carrinhoService;
    }


    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Object> inserirProduto(@RequestParam("id_produto") String id_produto,
                                                 @RequestParam("quantidade") String quantidadeString) {

        Optional<ProdutoModel> produtoOptional = null;
        try {
            produtoOptional = produtoService.buscarProdutoPorId(id_produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
        }

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        UsuarioModel usuario = usuarioService.pegarUsuarioLogado();
        Object validador = carrinhoService.validaInsercaoProduto(usuario, produtoOptional.get(), quantidadeString);
        if (validador instanceof String) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validador);
        }

        carrinhoService.inserirCarrinho((CarrinhoModel) validador);

        return ResponseEntity.status(HttpStatus.CREATED).body(verCarrinho().getBody());
    }

    @GetMapping
    public ResponseEntity<Object> verCarrinho() {

        UsuarioModel usuario = usuarioService.pegarUsuarioLogado();
        Optional<List<CarrinhoModel>> carrinhoExistente = carrinhoService.buscarCarrinhoDoUsuario(usuario);

        if (carrinhoExistente.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Carrinho vazio");
        }

        List<Object> listaCarrinhoView = carrinhoService.gerarVisualizacaoCarrinho(carrinhoExistente.get());

        return ResponseEntity.status(HttpStatus.OK).body( listaCarrinhoView);
    }


    @DeleteMapping("/{id_produto}")
    public ResponseEntity<Object> removeItemCarrinho(@PathVariable(value = "id_produto") String id_produto) {

        Optional<ProdutoModel> produtoOptional = null;
        try {
            produtoOptional = produtoService.buscarProdutoPorId(id_produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
        }

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        UsuarioModel usuario = usuarioService.pegarUsuarioLogado();
        Optional<CarrinhoModel> carrinhoExistente = carrinhoService
                .buscarProdutoDoUsuarioNoCarrinho(usuario, produtoOptional.get());

        if (carrinhoExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Produto não encontrado neste carrinho.");
        }

        carrinhoService.apagarItemCarrinho(carrinhoExistente.get());

        return ResponseEntity.status(HttpStatus.OK).body("Item " + carrinhoExistente.get().getProduto().getNome() + " removido.");
    }

    @DeleteMapping
    public ResponseEntity<Object> cancelaCarrinho() {
        UsuarioModel usuario = usuarioService.pegarUsuarioLogado();
        Optional<List<CarrinhoModel>> carrinhoExistente = carrinhoService.buscarCarrinhoDoUsuario(usuario);
        if (carrinhoExistente.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário " + usuario.getNome() + " não possui carrinho ativo.");
        }
        for (CarrinhoModel carrinho : carrinhoExistente.get()) {
            carrinhoService.apagarItemCarrinho(carrinho);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Carrinho cancelado.");
    }


    @PostMapping("/venda")
    public ResponseEntity<Object> confirmaVenda() {

        UsuarioModel usuario = usuarioService.pegarUsuarioLogado();
        Optional<List<CarrinhoModel>> carrinhoExistente = carrinhoService.buscarCarrinhoDoUsuario(usuario);

        if (carrinhoExistente.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário " + usuario.getNome() + " não possui carrinho ativo.");
        }

        Object validador = vendaService.efetivaVendaCompleta(usuario, carrinhoExistente.get());

        if (validador instanceof String) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(validador);
        }

        cancelaCarrinho();

        return ResponseEntity.status(HttpStatus.CREATED).body("Venda confirmada.");
    }
}
