package com.ecommerceapi.controllers;

import com.ecommerceapi.models.*;
import com.ecommerceapi.services.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/itens-carrinho")
@SecurityRequirement(name = "ecommerce")
@Tag(name = "Carrinho", description = "Gerencia o carrinho")
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
    @Operation(summary = "Insere e altera itens no carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inserção do item realizada com sucesso",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "[\n" +
                                    "  {\n" +
                                    "    \"produto\": {\n" +
                                    "      \"id\": \"8fcfe445-864c-4d96-b8c0-7d75acb45dc5\",\n" +
                                    "      \"nome\": \"Tênis B\",\n" +
                                    "      \"quantidade_estoque\": 91,\n" +
                                    "      \"preco\": 6.44,\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/produtos/id/8fcfe445-864c-4d96-b8c0-7d75acb45dc5\"\n" +
                                    "        }\n" +
                                    "      ]\n" +
                                    "    },\n" +
                                    "    \"quantidade\": 4,\n" +
                                    "    \"valorTotalProduto\": 25.76\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Quantidade de itens\": 1,\n" +
                                    "    \"Valor total do carrinho\": 25.76\n" +
                                    "  }\n" +
                                    "]")})),
            @ApiResponse(responseCode = "400", description = "Id inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    public ResponseEntity<Object> inserirProduto(
            @Parameter(content = @Content(examples = @ExampleObject("8fcfe445-864c-4d96-b8c0-7d75acb45dc5")))
            @RequestParam("id_produto") String id_produto,
            @Parameter(content = @Content(examples = @ExampleObject("4")))
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

    @DeleteMapping("/{id_produto}")
    @Operation(summary = "Remove um item do carrinho por ID", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Remoção do item realizado com sucesso",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "Item Tênis B removido.")})),
            @ApiResponse(responseCode = "400", description = "Id inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    public ResponseEntity<Object> removeItemCarrinho(
            @Parameter(content = @Content(examples = @ExampleObject("8fcfe445-864c-4d96-b8c0-7d75acb45dc5")))
            @PathVariable(value = "id_produto") String id_produto) {

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
    @Operation(summary = "Remove todos os itens do carrinho", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrinho cancelado.",
                    content = @Content(examples = @ExampleObject("Carrinho cancelado."))),
            @ApiResponse(responseCode = "400", description = "Carrinho inexistente.",
                    content = @Content(examples = @ExampleObject("Usuário Bernardo Kauê da Silva não possui carrinho ativo."))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content)
    })
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

    @GetMapping
    @Operation(summary = "Exibe os itens do carrinho", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exibição dos itens realizado com sucesso",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "[\n" +
                                    "  {\n" +
                                    "    \"produto\": {\n" +
                                    "      \"id\": \"f9c473bf-afb8-4eb2-9d32-d1c2b9498408\",\n" +
                                    "      \"nome\": \"Blusa B\",\n" +
                                    "      \"quantidade_estoque\": 326,\n" +
                                    "      \"preco\": 87.5,\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/produtos/id/f9c473bf-afb8-4eb2-9d32-d1c2b9498408\"\n" +
                                    "        }\n" +
                                    "      ]\n" +
                                    "    },\n" +
                                    "    \"quantidade\": 2,\n" +
                                    "    \"valorTotalProduto\": 175\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"produto\": {\n" +
                                    "      \"id\": \"b7545abe-eec2-4537-88fc-63667782789c\",\n" +
                                    "      \"nome\": \"Meia B\",\n" +
                                    "      \"quantidade_estoque\": 56,\n" +
                                    "      \"preco\": 6.44,\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/produtos/id/b7545abe-eec2-4537-88fc-63667782789c\"\n" +
                                    "        }\n" +
                                    "      ]\n" +
                                    "    },\n" +
                                    "    \"quantidade\": 7,\n" +
                                    "    \"valorTotalProduto\": 45.08\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"produto\": {\n" +
                                    "      \"id\": \"8fcfe445-864c-4d96-b8c0-7d75acb45dc5\",\n" +
                                    "      \"nome\": \"Tênis B\",\n" +
                                    "      \"quantidade_estoque\": 91,\n" +
                                    "      \"preco\": 6.44,\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/produtos/id/8fcfe445-864c-4d96-b8c0-7d75acb45dc5\"\n" +
                                    "        }\n" +
                                    "      ]\n" +
                                    "    },\n" +
                                    "    \"quantidade\": 4,\n" +
                                    "    \"valorTotalProduto\": 25.76\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Quantidade de itens\": 13,\n" +
                                    "    \"Quantidade de produtos\": 3,\n" +
                                    "    \"Valor total do carrinho\": 245.84\n" +
                                    "  }\n" +
                                    "]")})),
            @ApiResponse(responseCode = "400", description = "Carrinho inexistente.",
                    content = @Content(examples = @ExampleObject("Usuário Bernardo Kauê da Silva não possui carrinho ativo."))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content)
    })
    public ResponseEntity<Object> verCarrinho() {

        UsuarioModel usuario = usuarioService.pegarUsuarioLogado();
        Optional<List<CarrinhoModel>> carrinhoExistente = carrinhoService.buscarCarrinhoDoUsuario(usuario);

        if (carrinhoExistente.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário " + usuario.getNome() + " não possui carrinho ativo.");
        }

        List<Object> listaCarrinhoView = carrinhoService.gerarVisualizacaoCarrinho(carrinhoExistente.get());

        return ResponseEntity.status(HttpStatus.OK).body(listaCarrinhoView);
    }

    @PostMapping("/venda")
    @Operation(summary = "Efetiva a venda ao usuário", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Efetivação da venda realizada com sucesso",
                    content = @Content(examples = @ExampleObject("Venda confirmada."))),
            @ApiResponse(responseCode = "400", description = "Carrinho inexistente.",
                    content = @Content(examples = @ExampleObject("Usuário Bernardo Kauê da Silva não possui carrinho ativo."))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content),
            @ApiResponse(responseCode = "409", description = "Quantidade do produto no carrinho tornou-se maior que a disponível no estoque " +
                    "entre a inserção no carrinho e a efetivação da venda.",
                    content = @Content(examples = @ExampleObject("Quantidade de Tênis B indisponível no estoque.")))})
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
