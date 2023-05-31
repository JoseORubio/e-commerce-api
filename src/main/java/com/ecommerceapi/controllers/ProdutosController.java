package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.models.ClienteModel;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.services.ProdutoService;
import com.ecommerceapi.utils.ControllerUtils;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {

    final ProdutoService produtoService;

    public ProdutosController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Object> cadastrarProduto(@RequestBody @Valid ProdutoDTO produtoDTO, BindingResult bindingResult) {

        List<Map<String, String>> listaErros = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            var listaFieldErrors = bindingResult.getFieldErrors();
            for (FieldError erro : listaFieldErrors) {
                listaErros.add(ControllerUtils.adicionarErros(erro.getField(), erro.getDefaultMessage()));
            }
        }

        if (produtoService.existsByNome(produtoDTO.getNome())) {
            listaErros.add(ControllerUtils.adicionarErros("nome", "Nome já utilizado."));
        }

        if (!listaErros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(listaErros);
        }

        ProdutoModel produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDTO, produtoModel);

        produtoModel.setQuantidade_estoque(Integer.parseInt(produtoDTO.getQuantidade_estoque()));
        produtoModel.setPreco(BigDecimal.valueOf(
                Double.parseDouble(produtoDTO.getPreco().replace(',', '.'))));

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvarProduto(produtoModel));
    }


    @GetMapping
    public ResponseEntity<List<ProdutoModel>> buscarProdutos() {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscarProdutos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarProdutosPorId(@PathVariable(value = "id") String id) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        Optional<ProdutoModel> produtoOptional = produtoService.buscarProdutoPorId(uuid);
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(produtoOptional.get());
    }

    @GetMapping("/pesquisar/{nome}")
    public ResponseEntity<Object> pesquisarProdutos(@PathVariable(value = "nome") String nome) {
        Optional<List<ProdutoModel>> produtoOptional = produtoService.pesquisarProdutos(nome);
        if (produtoOptional.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(produtoOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarProduto(@PathVariable(value = "id") String id) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        Optional<ProdutoModel> produtoOptional = produtoService.buscarProdutoPorId(uuid);
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        produtoService.apagarProduto(produtoOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto apagado com sucesso.");
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizarProduto(@PathVariable(value = "id") String id,
                                                   @RequestBody @Valid ProdutoDTO produtoDTO, BindingResult bindingResult) {

        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        Optional<ProdutoModel> produtoOptional = produtoService.buscarProdutoPorId(uuid);
        if (!produtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        List<Map<String, String>> listaErros = new ArrayList<>();
        List<String> listaNulos = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            var listaFieldErrors = bindingResult.getFieldErrors();
            for (FieldError erro : listaFieldErrors) {

                if (erro.getRejectedValue() == null) {
                    listaNulos.add(erro.getField());
                } else {
                    listaErros.add(ControllerUtils.adicionarErros(erro.getField(), erro.getDefaultMessage()));
                }
            }
        }

        if (produtoDTO.getNome()!= null
                && !produtoDTO.getNome().equals(produtoOptional.get().getNome())
                && produtoService.existsByNome(produtoDTO.getNome())) {
            listaErros.add(ControllerUtils.adicionarErros("nome", "Nome já utilizado."));
        }

        if (!listaErros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(listaErros);
        }

        ProdutoModel produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDTO, produtoModel);
        produtoModel.setId(uuid);

        for (String campo : listaNulos) {
            switch (campo) {
                case "nome" : produtoModel.setNome(produtoOptional.get().getNome()); break;
                case "quantidade_estoque": produtoModel.setQuantidade_estoque(produtoOptional.get().getQuantidade_estoque()); break;
                case "preco" : produtoModel.setPreco(produtoOptional.get().getPreco());break;
            }
//            if (campo.equals("nome")) produtoModel.setNome(produtoOptional.get().getNome());
//            if (campo.equals("quantidade_estoque"))
//                produtoModel.setQuantidade_estoque(produtoOptional.get().getQuantidade_estoque());
//            if (campo.equals("preco")) produtoModel.setPreco(produtoOptional.get().getPreco());
        }

        if (produtoDTO.getQuantidade_estoque() != null)
            produtoModel.setQuantidade_estoque(Integer.parseInt(produtoDTO.getQuantidade_estoque()));
        if (produtoDTO.getPreco() != null)
        produtoModel.setPreco(BigDecimal.valueOf(
                Double.parseDouble(produtoDTO.getPreco().replace(',', '.'))));

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvarProduto(produtoModel));
    }


}
