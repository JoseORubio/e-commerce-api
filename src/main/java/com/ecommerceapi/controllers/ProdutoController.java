package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.services.ProdutoService;
import com.ecommerceapi.utils.ManipuladorListaErros;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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
@Tag(name = "Produtos", description = "Gerencia os produtos")
public class ProdutoController {
    final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @Operation(summary = "Cadastra um produto", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro do produto realizado com sucesso",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "{\"id\": \"9da57f64-dc4b-4d6d-b39b-426ffea66e3b\",\n" +
                                    "    \"nome\": \"Blusa C\",\n" +
                                    "    \"quantidadeEstoque\": 76,\n" +
                                    "    \"preco\": 38.99\n" +
                                    "}")})),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos ou já cadastrados",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "[\n" +
                                    "    {\n" +
                                    "        \"Campo\": \"preco\",\n" +
                                    "        \"Erros\": \"Não deve estar em branco. Deve ser qualquer valor monetário até 9999,99 com dois dígitos após , ou .\"\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "        \"Campo\": \"quantidadeEstoque\",\n" +
                                    "        \"Erros\": \"Não deve estar em branco. Deve ser qualquer número entre 0 e 999999.\"\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "        \"Campo\": \"nome\",\n" +
                                    "        \"Erros\": \"Pode conter qualquer letra, número e os seguintes caracteres: , . /  # ( ) -. Não deve estar em branco.\"\n" +
                                    "    }\n" +
                                    "]")})),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content)})
    @SecurityRequirement(name = "ecommerce")
    public ResponseEntity<Object> cadastrarProduto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "{ \"nome\" : \"Blusa C\",\n" +
                                    "    \"quantidadeEstoque\" : \"76\",\n" +
                                    "    \"preco\": \"38,99\"}")
                    }))
            @RequestBody @Valid ProdutoDTO produtoDTO, BindingResult errosDeValidacao) {

        ProdutoModel produtoModel = null;
        try {
            produtoModel = produtoService.validaCadastroProduto(produtoDTO, errosDeValidacao);
        } catch (IllegalArgumentException e) {
            JsonNode mensagensErros = ManipuladorListaErros.converteStringJsonParaJsonNode(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagensErros);
        }

        produtoService.salvarProduto(produtoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoModel);
    }


    @PutMapping("/{id_produto}")
    @Operation(summary = "Atualiza um produto", method = "PUT")
    @SecurityRequirement(name = "ecommerce")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização do produto realizada com sucesso",
                    content = @Content(examples = {@ExampleObject(
                            value = "{\n" +
                                    "    \"id\": \"9da57f64-dc4b-4d6d-b39b-426ffea66e3b\",\n" +
                                    "    \"nome\": \"Blusa C\",\n" +
                                    "    \"quantidadeEstoque\": 36,\n" +
                                    "    \"preco\": 87.5\n" +
                                    "}")})),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos ou já cadastrados",
                    content = @Content(examples = {@ExampleObject(
                            value = "[\n" +
                                    "    {\n" +
                                    "        \"Campo\": \"preco\",\n" +
                                    "        \"Erros\": \"Não deve estar em branco. Deve ser qualquer valor monetário até 9999,99 com dois dígitos após , ou .\"\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "        \"Campo\": \"quantidadeEstoque\",\n" +
                                    "        \"Erros\": \"Não deve estar em branco. Deve ser qualquer número entre 0 e 999999.\"\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "        \"Campo\": \"nome\",\n" +
                                    "        \"Erros\": \"Pode conter qualquer letra, número e os seguintes caracteres: , . /  # ( ) -. Não deve estar em branco.\"\n" +
                                    "    }\n" +
                                    "]")})),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    public ResponseEntity<Object> atualizarProduto(
            @Parameter(content = @Content(examples = @ExampleObject("9da57f64-dc4b-4d6d-b39b-426ffea66e3b")))
            @PathVariable(value = "id_produto") String idProduto,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "{\n" +
                                    "   \"nome\" : \"Blusa C\",\n" +
                                    "   \"quantidadeEstoque\" : \"36\",\n" +
                                    "   \"preco\": \"87,50\"\n" +
                                    "}")}))
            @RequestBody @Valid ProdutoDTO produtoDTO, BindingResult errosDeValidacao) {

        Optional<ProdutoModel> produtoOptional = null;
        try {
            produtoOptional = produtoService.buscarProdutoPorId(idProduto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida.");
        }

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        ProdutoModel produtoModel = null;

        try {
            produtoModel = produtoService.validaAtualizacaoProduto(produtoOptional.get(), produtoDTO, errosDeValidacao);
        } catch (IllegalArgumentException e) {
            JsonNode mensagensErros = ManipuladorListaErros.converteStringJsonParaJsonNode(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagensErros);
        }

        produtoService.salvarProduto(produtoModel);
        return ResponseEntity.status(HttpStatus.OK).body(produtoModel);
    }

    @DeleteMapping("/{id_produto}")
    @Operation(summary = "Remove um produto", method = "DELETE")
    @SecurityRequirement(name = "ecommerce")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Remoção de produto realizado com sucesso",
                    content = @Content(examples = @ExampleObject("Produto Calça apagado com sucesso."))),
            @ApiResponse(responseCode = "400", description = "Id inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    public ResponseEntity<Object> removerProduto(
            @Parameter(content = @Content(examples = @ExampleObject("bc4c0129-e190-4d5c-bf42-780585b74edf")))
            @PathVariable(value = "id_produto") String idProduto) {

        Optional<ProdutoModel> produtoOptional = null;
        try {
            produtoOptional = produtoService.buscarProdutoPorId(idProduto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida.");
        }

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        produtoService.apagarProduto(produtoOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto " + produtoOptional.get().getNome() + " apagado com sucesso.");
    }

    @GetMapping
    @Operation(summary = "Busca os produtos", method = "GET")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Busca de produtos realizada com sucesso",
            content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                    value = "{\n" +
                            "    \"content\": [\n" +
                            "        {\n" +
                            "            \"id\": \"b47861f2-5ae0-4771-9d52-8d45bfec6acb\",\n" +
                            "            \"nome\": \"Bermuda A\",\n" +
                            "            \"quantidadeEstoque\": 410,\n" +
                            "            \"preco\": 900.00,\n" +
                            "            \"links\": [\n" +
                            "                {\n" +
                            "                    \"rel\": \"self\",\n" +
                            "                    \"href\": \"http://localhost:8080/produtos/id/b47861f2-5ae0-4771-9d52-8d45bfec6acb\"\n" +
                            "                }\n" +
                            "            ]\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": \"6bb93022-563a-4e01-ac6f-9752559447b7\",\n" +
                            "            \"nome\": \"Bermuda B\",\n" +
                            "            \"quantidadeEstoque\": 84,\n" +
                            "            \"preco\": 6.44,\n" +
                            "            \"links\": [\n" +
                            "                {\n" +
                            "                    \"rel\": \"self\",\n" +
                            "                    \"href\": \"http://localhost:8080/produtos/id/6bb93022-563a-4e01-ac6f-9752559447b7\"\n" +
                            "                }\n" +
                            "            ]\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": \"02e177c8-1d32-4ffb-984f-63e3e8a4e966\",\n" +
                            "            \"nome\": \"Blusa A\",\n" +
                            "            \"quantidadeEstoque\": 495,\n" +
                            "            \"preco\": 10.00,\n" +
                            "            \"links\": [\n" +
                            "                {\n" +
                            "                    \"rel\": \"self\",\n" +
                            "                    \"href\": \"http://localhost:8080/produtos/id/02e177c8-1d32-4ffb-984f-63e3e8a4e966\"\n" +
                            "                }\n" +
                            "            ]\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": \"f9c473bf-afb8-4eb2-9d32-d1c2b9498408\",\n" +
                            "            \"nome\": \"Blusa B\",\n" +
                            "            \"quantidadeEstoque\": 500,\n" +
                            "            \"preco\": 10.00,\n" +
                            "            \"links\": [\n" +
                            "                {\n" +
                            "                    \"rel\": \"self\",\n" +
                            "                    \"href\": \"http://localhost:8080/produtos/id/f9c473bf-afb8-4eb2-9d32-d1c2b9498408\"\n" +
                            "                }\n" +
                            "            ]\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"id\": \"9da57f64-dc4b-4d6d-b39b-426ffea66e3b\",\n" +
                            "            \"nome\": \"Blusa C\",\n" +
                            "            \"quantidadeEstoque\": 36,\n" +
                            "            \"preco\": 87.50,\n" +
                            "            \"links\": [\n" +
                            "                {\n" +
                            "                    \"rel\": \"self\",\n" +
                            "                    \"href\": \"http://localhost:8080/produtos/id/9da57f64-dc4b-4d6d-b39b-426ffea66e3b\"\n" +
                            "                }\n" +
                            "            ]\n" +
                            "        }\n" +
                            "    ],\n" +
                            "    \"pageable\": {\n" +
                            "        \"sort\": {\n" +
                            "            \"empty\": false,\n" +
                            "            \"unsorted\": false,\n" +
                            "            \"sorted\": true\n" +
                            "        },\n" +
                            "        \"offset\": 0,\n" +
                            "        \"pageSize\": 5,\n" +
                            "        \"pageNumber\": 0,\n" +
                            "        \"paged\": true,\n" +
                            "        \"unpaged\": false\n" +
                            "    },\n" +
                            "    \"last\": false,\n" +
                            "    \"totalPages\": 4,\n" +
                            "    \"totalElements\": 18,\n" +
                            "    \"size\": 5,\n" +
                            "    \"number\": 0,\n" +
                            "    \"sort\": {\n" +
                            "        \"empty\": false,\n" +
                            "        \"unsorted\": false,\n" +
                            "        \"sorted\": true\n" +
                            "    },\n" +
                            "    \"first\": true,\n" +
                            "    \"numberOfElements\": 5,\n" +
                            "    \"empty\": false\n" +
                            "}")})))
    @Parameter(in = ParameterIn.QUERY, description = "Página", name = "page"
            , content = @Content(schema = @Schema(type = "integer", defaultValue = "0")))
    @Parameter(in = ParameterIn.QUERY, description = "Resultados por página", name = "size"
            , content = @Content(schema = @Schema(type = "integer", defaultValue = "5")))
    @Parameter(in = ParameterIn.QUERY, description = "Critério de ordenação. Padronizado pelo campo \"nome\" ascendente. É possível adicionar múltiplos critérios."
            , name = "sort")
    public ResponseEntity<Page<ProdutoModel>> buscarProdutos(
            @ParameterObject
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
    @Operation(summary = "Busca um produto por ID", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Busca de produto realizado com sucesso",
            content = @Content(examples = {@ExampleObject(
                    value = "{\n" +
                            "  \"id\": \"f9c473bf-afb8-4eb2-9d32-d1c2b9498408\",\n" +
                            "  \"nome\": \"Blusa B\",\n" +
                            "  \"quantidadeEstoque\": 500,\n" +
                            "  \"preco\": 10,\n" +
                            "  \"_links\": {\n" +
                            "    \"Lista de Produtos\": {\n" +
                            "      \"href\": \"http://localhost:8080/produtos\"\n" +
                            "    }\n" +
                            "  }\n" +
                            "}")})),
            @ApiResponse(responseCode = "400", description = "Id inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    public ResponseEntity<Object> buscarProdutoPorId(
            @Parameter(content = @Content(examples = @ExampleObject("f9c473bf-afb8-4eb2-9d32-d1c2b9498408")))
            @PathVariable(value = "id_produto") String idProduto) {

        Optional<ProdutoModel> produtoOptional = null;
        try {
            produtoOptional = produtoService.buscarProdutoPorId(idProduto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida.");
        }

        if (produtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }

        produtoOptional.get().add(linkTo(methodOn(ProdutoController.class).buscarProdutos(Pageable.unpaged()))
                .withRel("Lista de Produtos"));

        return ResponseEntity.status(HttpStatus.OK).body(produtoOptional.get());
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Busca os produtos por nome", method = "GET")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Busca de produto realizado com sucesso",
            content = @Content(examples = {@ExampleObject(
                    value = "{\n" +
                            "  \"content\": [\n" +
                            "    {\n" +
                            "      \"id\": \"b47861f2-5ae0-4771-9d52-8d45bfec6acb\",\n" +
                            "      \"nome\": \"Bermuda A\",\n" +
                            "      \"quantidadeEstoque\": 410,\n" +
                            "      \"preco\": 900,\n" +
                            "      \"links\": [\n" +
                            "        {\n" +
                            "          \"rel\": \"self\",\n" +
                            "          \"href\": \"http://localhost:8080/produtos/id/b47861f2-5ae0-4771-9d52-8d45bfec6acb\"\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"id\": \"6bb93022-563a-4e01-ac6f-9752559447b7\",\n" +
                            "      \"nome\": \"Bermuda B\",\n" +
                            "      \"quantidadeEstoque\": 84,\n" +
                            "      \"preco\": 6.44,\n" +
                            "      \"links\": [\n" +
                            "        {\n" +
                            "          \"rel\": \"self\",\n" +
                            "          \"href\": \"http://localhost:8080/produtos/id/6bb93022-563a-4e01-ac6f-9752559447b7\"\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"pageable\": {\n" +
                            "    \"sort\": {\n" +
                            "      \"empty\": false,\n" +
                            "      \"sorted\": true,\n" +
                            "      \"unsorted\": false\n" +
                            "    },\n" +
                            "    \"offset\": 0,\n" +
                            "    \"pageSize\": 5,\n" +
                            "    \"pageNumber\": 0,\n" +
                            "    \"paged\": true,\n" +
                            "    \"unpaged\": false\n" +
                            "  },\n" +
                            "  \"last\": true,\n" +
                            "  \"totalElements\": 2,\n" +
                            "  \"totalPages\": 1,\n" +
                            "  \"size\": 5,\n" +
                            "  \"number\": 0,\n" +
                            "  \"sort\": {\n" +
                            "    \"empty\": false,\n" +
                            "    \"sorted\": true,\n" +
                            "    \"unsorted\": false\n" +
                            "  },\n" +
                            "  \"first\": true,\n" +
                            "  \"numberOfElements\": 2,\n" +
                            "  \"empty\": false\n" +
                            "}")})),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    })
    @Parameter(in = ParameterIn.QUERY, description = "Página", name = "page"
            , content = @Content(schema = @Schema(type = "integer", defaultValue = "0")))
    @Parameter(in = ParameterIn.QUERY, description = "Resultados por página", name = "size"
            , content = @Content(schema = @Schema(type = "integer", defaultValue = "5")))
    @Parameter(in = ParameterIn.QUERY, description = "Critério de ordenação. Padronizado pelo campo \"nome\" ascendente. É possível adicionar múltiplos critérios."
            , name = "sort")
    public ResponseEntity<Object> buscarProdutosPorNome(
            @Parameter(example = "Bermuda")
            @PathVariable(value = "nome") String nome,
            @ParameterObject
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
