package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.services.ProdutoService;
import com.ecommerceapi.utils.ConversorUUID;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

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

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvarProduto(produtoModel));
    }
//    }    @PostMapping
//    public ResponseEntity<Object> cadastrarProduto(@RequestBody @Valid ProdutoDTO produtoDTO, BindingResult bindingResult) {
//
//        List<Map<String, String>> listaErros = new ArrayList<>();
//        if (bindingResult.hasErrors()) {
//            var listaFieldErrors = bindingResult.getFieldErrors();
//            for (FieldError erro : listaFieldErrors) {
//                listaErros.add(ConversorUUID.adicionarErros(erro.getField(), erro.getDefaultMessage()));
//            }
//        }
//
//        if (produtoService.existsByNome(produtoDTO.getNome())) {
//            listaErros.add(ConversorUUID.adicionarErros("nome", "Nome já utilizado."));
//        }
//
//        if (!listaErros.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listaErros);
//        }
//
//        ProdutoModel produtoModel = new ProdutoModel();
//        BeanUtils.copyProperties(produtoDTO, produtoModel);
//
//        produtoModel.setQuantidade_estoque(Integer.parseInt(produtoDTO.getQuantidade_estoque()));
//        produtoModel.setPreco(BigDecimal.valueOf(
//                Double.parseDouble(produtoDTO.getPreco().replace(',', '.'))));
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.salvarProduto(produtoModel));
//    }

    @PutMapping("/{id_produto}")
    public ResponseEntity<Object> atualizarProduto(@PathVariable(value = "id_produto") String id_produto,
                                                   @RequestBody @Valid ProdutoDTO produtoDTO, BindingResult errosDeValidacao) {

//        UUID id = ConversorUUID.converteUUID(id_produto);
//        if (id_produto.equals("") || id == null)
        Optional<ProdutoModel> produtoOptional = null;
        try {
            produtoOptional = produtoService.buscarProdutoPorId(id_produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
        }
//        Optional<ProdutoModel> produtoOptional = produtoService.buscarProdutoPorId(id);

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }


        Object validador = produtoService
                .validaAtualizacaoProduto(produtoOptional.get(),produtoDTO, errosDeValidacao);

        if (validador instanceof List<?>) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validador);
        }
        ProdutoModel produtoModel = (ProdutoModel) validador;
        produtoService.salvarProduto(produtoModel);


//        List<Map<String, String>> listaErros = new ArrayList<>();
//        List<String> listaNulos = new ArrayList<>();

//        if (bindingResult.hasErrors()) {
//            var listaFieldErrors = bindingResult.getFieldErrors();
//            for (FieldError erro : listaFieldErrors) {
//                if (erro.getRejectedValue() == null) {
//                    listaNulos.add(erro.getField());
//                } else {
//                    listaErros.add(ConversorUUID.adicionarErros(erro.getField(), erro.getDefaultMessage()));
//                }
//            }
//        }
//
//        if (produtoDTO.getNome() != null
//                && !produtoDTO.getNome().equals(produtoOptional.get().getNome())
//                && produtoService.existsByNome(produtoDTO.getNome())) {
//            listaErros.add(ConversorUUID.adicionarErros("nome", "Nome já utilizado."));
//        }

//        if (!listaErros.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(listaErros);
//        }

//        ProdutoModel produtoModel = new ProdutoModel();
//        BeanUtils.copyProperties(produtoDTO, produtoModel);
//        produtoModel.setId(id);

//        for (String campo : listaNulos) {
//            switch (campo) {
//                case "nome":
//                    produtoModel.setNome(produtoOptional.get().getNome());
//                    break;
//                case "quantidade_estoque":
//                    produtoModel.setQuantidade_estoque(produtoOptional.get().getQuantidade_estoque());
//                    break;
//                case "preco":
//                    produtoModel.setPreco(produtoOptional.get().getPreco());
//                    break;
//            }
//        }

//        if (produtoDTO.getQuantidade_estoque() != null)
//            produtoModel.setQuantidade_estoque(Integer.parseInt(produtoDTO.getQuantidade_estoque()));
//        if (produtoDTO.getPreco() != null)
//            produtoModel.setPreco(BigDecimal.valueOf(
//                    Double.parseDouble(produtoDTO.getPreco().replace(',', '.'))));

        return ResponseEntity.status(HttpStatus.OK).body(produtoModel);
    }

//    @DeleteMapping("/{id_produto}")
//    public ResponseEntity<Object> deletarProduto(@PathVariable(value = "id_produto") String id_produto) {
//
//        UUID id = ConversorUUID.converteUUID(id_produto);
//        if (id_produto.equals("") || id == null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
//
//        Optional<ProdutoModel> produtoOptional = produtoService.buscarProdutoPorId(id);
//        if (!produtoOptional.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
//        }
//
//        produtoService.apagarProduto(produtoOptional.get());
//        return ResponseEntity.status(HttpStatus.OK).body("Produto apagado com sucesso.");
//    }

    @GetMapping
    public ResponseEntity<List<ProdutoModel>> buscarProdutos() {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.buscarProdutos());
    }

//    @GetMapping("/id/{id_produto}")
//    public ResponseEntity<Object> buscarProdutoPorId(@PathVariable(value = "id_produto") String id_produto) {
//
//
//        UUID id = ConversorUUID.converteUUID(id_produto);
//        if (id_produto.equals("") || id == null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");
//
//        Optional<ProdutoModel> produtoOptional = produtoService.buscarProdutoPorId(id);
//        if (!produtoOptional.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(produtoOptional.get());
//    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Object> buscarProdutosPorNome(@PathVariable(value = "nome") String nome) {
        Optional<List<ProdutoModel>> produtoOptional = produtoService.pesquisarProdutos(nome);
        if (produtoOptional.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(produtoOptional.get());
    }


}
