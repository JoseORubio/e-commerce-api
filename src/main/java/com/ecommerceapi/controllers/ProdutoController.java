package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Object> cadastrarProduto(@RequestBody @Valid ProdutoDTO produtoDTO, BindingResult errosDeValidacao) {

        Object validador = produtoService.validaCadastroProduto(produtoDTO, errosDeValidacao);

        if (validador instanceof List<?>) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validador);
        }

        ProdutoModel produtoModel = (ProdutoModel) validador;
        produtoService.salvarProduto(produtoModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoModel);
    }


    @PutMapping("/{id_produto}")
    public ResponseEntity<Object> atualizarProduto(@PathVariable(value = "id_produto") String id_produto,
                                                   @RequestBody @Valid ProdutoDTO produtoDTO, BindingResult errosDeValidacao) {

        Optional<ProdutoModel> produtoOptional = null;
        try {
            produtoOptional = produtoService.buscarProdutoPorId(id_produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
        }

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        Object validador = produtoService.validaAtualizacaoProduto(produtoOptional.get(), produtoDTO, errosDeValidacao);

        if (validador instanceof List<?>) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validador);
        }
        ProdutoModel produtoModel = (ProdutoModel) validador;
        produtoService.salvarProduto(produtoModel);

        return ResponseEntity.status(HttpStatus.OK).body(produtoModel);
    }

    @DeleteMapping("/{id_produto}")
    public ResponseEntity<Object> deletarProduto(@PathVariable(value = "id_produto") String id_produto) {

        Optional<ProdutoModel> produtoOptional = null;
        try {
            produtoOptional = produtoService.buscarProdutoPorId(id_produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
        }

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        produtoService.apagarProduto(produtoOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto " + produtoOptional.get().getNome() + " apagado com sucesso.");
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoModel>> buscarProdutos(
            @PageableDefault(page = 0, size = 5, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ProdutoModel> listaProdutos = produtoService.buscarProdutos(pageable);
        if (!listaProdutos.isEmpty()) {
            for (ProdutoModel produto : listaProdutos) {
                produto.add(linkTo(
                        methodOn(ProdutoController.class).buscarProdutoPorId(produto.getId().toString()))
                        .withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(listaProdutos);
    }

    @GetMapping("/id/{id_produto}")
    public ResponseEntity<Object> buscarProdutoPorId(@PathVariable(value = "id_produto") String id_produto) {


        Optional<ProdutoModel> produtoOptional = null;
        try {
            produtoOptional = produtoService.buscarProdutoPorId(id_produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
        }

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        produtoOptional.get().add(linkTo(methodOn(ProdutoController.class).buscarProdutos(Pageable.unpaged()))
                .withRel("Lista de Produtos"));

        return ResponseEntity.status(HttpStatus.OK).body(produtoOptional.get());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Object> buscarProdutosPorNome(@PathVariable(value = "nome") String nome,
    @PageableDefault(page = 0, size = 5, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Optional<Page<ProdutoModel>> produtoOptional = produtoService.pesquisarProdutos(nome, pageable);
        if (produtoOptional.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        for (ProdutoModel produto : produtoOptional.get()) {
            produto.add(linkTo(
                            methodOn(ProdutoController.class).buscarProdutoPorId(produto.getId().toString()))
                        .withSelfRel());
        }
        return ResponseEntity.status(HttpStatus.OK).body(produtoOptional.get());
    }


}
