package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.dtos.UsuarioViewDTO;
import com.ecommerceapi.models.PapelDoUsuarioModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.services.PapelDoUsuarioService;
import com.ecommerceapi.services.PapelService;
import com.ecommerceapi.services.UsuarioService;
import com.ecommerceapi.utils.ManipuladorListaErros;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping("/usuarios")
//@SecurityRequirement(name = "ecommerce")
@Tag(name = "Usuários", description = "Gerencia os usuários")
public class UsuarioController {

    final UsuarioService usuarioService;
    final PapelDoUsuarioService papelDoUsuarioService;
    final PapelService papelService;

    public UsuarioController(UsuarioService usuarioService, PapelDoUsuarioService papelDoUsuarioService, PapelService papelService) {
        this.usuarioService = usuarioService;
        this.papelDoUsuarioService = papelDoUsuarioService;
        this.papelService = papelService;
    }


    @PostMapping
    @Operation(summary = "Cadastra um usuário", method = "POST")
    @SecurityRequirements()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro do usuário realizado com sucesso",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "{\n" +
                                    "  \"nome\": \"Heloisa Yasmin Costa\",\n" +
                                    "  \"login\": \"heloi123\",\n" +
                                    "  \"cpf\": \"70181125404\",\n" +
                                    "  \"dataNasc\": \"06/03/1945\",\n" +
                                    "  \"sexo\": \"F\",\n" +
                                    "  \"telefone\": \"(85)98981-2256\",\n" +
                                    "  \"email\": \"heloisa_costa@ciaimoveissjc.com\",\n" +
                                    "  \"cep\": \"61604-270\",\n" +
                                    "  \"uf\": \"CE\",\n" +
                                    "  \"cidade\": \"Caucaia\",\n" +
                                    "  \"rua\": \"Rua Sargento Feitosa\",\n" +
                                    "  \"numeroRua\": 981\n" +
                                    "}")})),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos ou já cadastrados",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "[\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"sexo\",\n" +
                                    "    \"Erros\": \"Apenas 'M' para masculino, 'F' para feminino e 'O' para outro. Não deve estar em branco.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"telefone\",\n" +
                                    "    \"Erros\": \"Deve seguir o padrão, contendo DDD: (12)12345-1234. Não deve estar em branco.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"nome\",\n" +
                                    "    \"Erros\": \"Não deve estar em branco. Deve conter apenas letras e obedeçer o padrão 'Ana da Silva Pereira' com ao menos a primeira letra maiúscula.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"cep\",\n" +
                                    "    \"Erros\": \"Siga o padrão 12345-678. Não deve estar vazio. CEP não existe.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"login\",\n" +
                                    "    \"Erros\": \"Deve conter apenas números ou '_', e pelo menos uma letra minúscula. Não deve estar em branco. Login deve ter entre 8 e 20 caracteres.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"numeroRua\",\n" +
                                    "    \"Erros\": \"Deve ser qualquer número entre 1 e 99999. Não deve estar em branco.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"cpf\",\n" +
                                    "    \"Erros\": \"Não deve estar em branco. Número do registro de contribuinte individual brasileiro (CPF) inválido.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"email\",\n" +
                                    "    \"Erros\": \"Não deve estar em branco.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"senha\",\n" +
                                    "    \"Erros\": \"Senha deve ter entre 8 e 20 caracteres. Não deve estar em branco. Deve conter ao menos uma letra maiúscula, uma minúscula e um número.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"dataNasc\",\n" +
                                    "    \"Erros\": \"Não deve estar em branco. Deve estar no formato: 'dd/MM/yyyy'. Data inválida.\"\n" +
                                    "  }\n" +
                                    "]")}))})
    public ResponseEntity<Object> cadastrarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "{\n" +
                                    "  \"nome\": \"Heloisa Yasmin Costa\",\n" +
                                    "  \"login\": \"heloi123\",\n" +
                                    "  \"senha\": \"asd123aaD\",\n" +
                                    "  \"cpf\": \"70181125404\",\n" +
                                    "  \"dataNasc\": \"06/03/1945\",\n" +
                                    "  \"sexo\": \"F\",\n" +
                                    "  \"telefone\": \"(85)98981-2256\",\n" +
                                    "  \"email\": \"heloisa_costa@ciaimoveissjc.com\",\n" +
                                    "  \"cep\": \"61604-270\",\n" +
                                    "  \"numeroRua\": \"981\"\n" +
                                    "}")
                    }))
            @RequestBody @Valid UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {


        UsuarioModel usuarioModel = null;
        try {
            usuarioModel = usuarioService.validaCadastroUsuario(usuarioDTO, errosDeValidacao);
        } catch (IllegalArgumentException e) {
            JsonNode mensagensErros = ManipuladorListaErros.converteStringJsonParaJsonNode(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagensErros);
        }

        usuarioService.salvarUsuario(usuarioModel);
        papelDoUsuarioService.salvarPapelDoUsuario(
                new PapelDoUsuarioModel(usuarioModel.getId(), papelService.pegarIdPapelUsuario()));

        UsuarioViewDTO usuarioViewDTO = usuarioService.mostrarUsuarioLogado(usuarioModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioViewDTO);
    }

    @PutMapping
    @Operation(summary = "Atualiza o usuário logado", method = "PUT")
    @SecurityRequirement(name = "ecommerce")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização do usuário realizada com sucesso",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "{\n" +
                                    "  \"nome\": \"Heloisa Yasmin Costa\",\n" +
                                    "  \"login\": \"heloi123\",\n" +
                                    "  \"senha\": \"asd123aaD\",\n" +
                                    "  \"cpf\": \"70181125404\",\n" +
                                    "  \"dataNasc\": \"06/03/1945\",\n" +
                                    "  \"sexo\": \"F\",\n" +
                                    "  \"telefone\": \"(85)98981-2256\",\n" +
                                    "  \"email\": \"heloisa_costa@ciaimoveissjc.com\",\n" +
                                    "  \"cep\": \"61604-270\",\n" +
                                    "  \"numeroRua\": \"981\"\n" +
                                    "}")})),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos ou já cadastrados",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "[\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"sexo\",\n" +
                                    "    \"Erros\": \"Apenas 'M' para masculino, 'F' para feminino e 'O' para outro. Não deve estar em branco.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"telefone\",\n" +
                                    "    \"Erros\": \"Deve seguir o padrão, contendo DDD: (12)12345-1234. Não deve estar em branco.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"nome\",\n" +
                                    "    \"Erros\": \"Não deve estar em branco. Deve conter apenas letras e obedeçer o padrão 'Ana da Silva Pereira' com ao menos a primeira letra maiúscula.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"cep\",\n" +
                                    "    \"Erros\": \"Siga o padrão 12345-678. Não deve estar vazio. CEP não existe.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"login\",\n" +
                                    "    \"Erros\": \"Deve conter apenas números ou '_', e pelo menos uma letra minúscula. Não deve estar em branco. Login deve ter entre 8 e 20 caracteres.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"numeroRua\",\n" +
                                    "    \"Erros\": \"Deve ser qualquer número entre 1 e 99999. Não deve estar em branco.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"cpf\",\n" +
                                    "    \"Erros\": \"Não deve estar em branco. Número do registro de contribuinte individual brasileiro (CPF) inválido.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"email\",\n" +
                                    "    \"Erros\": \"Não deve estar em branco.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"senha\",\n" +
                                    "    \"Erros\": \"Senha deve ter entre 8 e 20 caracteres. Não deve estar em branco. Deve conter ao menos uma letra maiúscula, uma minúscula e um número.\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"Campo\": \"dataNasc\",\n" +
                                    "    \"Erros\": \"Não deve estar em branco. Deve estar no formato: 'dd/MM/yyyy'. Data inválida.\"\n" +
                                    "  }\n" +
                                    "]")})),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)})
    public ResponseEntity<Object> atualizarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "{\n" +
                                    "  \"nome\": \"Heloisa Yasmin Costa\",\n" +
                                    "  \"login\": \"heloi123\",\n" +
                                    "  \"senha\": \"asd123aaD\",\n" +
                                    "  \"cpf\": \"70181125404\",\n" +
                                    "  \"dataNasc\": \"06/03/1945\",\n" +
                                    "  \"sexo\": \"F\",\n" +
                                    "  \"telefone\": \"(85)98981-2256\",\n" +
                                    "  \"email\": \"heloisa_costa@ciaimoveissjc.com\",\n" +
                                    "  \"cep\": \"61604-270\",\n" +
                                    "  \"numeroRua\": \"981\"\n" +
                                    "}")
                    }))
            @RequestBody @Valid UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {

        UsuarioModel usuarioLogado = usuarioService.pegarUsuarioLogado();

        UsuarioModel usuarioModel = null;
        try {
            usuarioModel = usuarioService.validaAtualizacaoUsuario(usuarioLogado, usuarioDTO, errosDeValidacao);
        } catch (IllegalArgumentException e) {
            JsonNode mensagensErros = ManipuladorListaErros.converteStringJsonParaJsonNode(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensagensErros);
        }

        usuarioService.salvarUsuario(usuarioModel);
        papelDoUsuarioService.salvarPapelDoUsuario(
                new PapelDoUsuarioModel(usuarioModel.getId(), papelService.pegarIdPapelUsuario()));
        UsuarioViewDTO usuarioViewDTO = usuarioService.mostrarUsuarioLogado(usuarioModel);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioViewDTO);
    }


    @DeleteMapping("/{id_usuario}")
    @Operation(summary = "Remove um usuário por ID", method = "DELETE")
    @SecurityRequirement(name = "ecommerce")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Remoção de usuário realizado com sucesso",
                    content = @Content(examples = @ExampleObject("Usuário Heloisa Yasmin Costa apagado com sucesso."))),
            @ApiResponse(responseCode = "400", description = "Id inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)})
    public ResponseEntity<Object> deletarUsuarioPorId(
            @Parameter(content = @Content(examples = @ExampleObject("f9c473bf-afb8-4eb2-9d32-d1c2b9498408")))
            @PathVariable(value = "id_usuario") String idUsuario) {

        Optional<UsuarioModel> usuarioOptional = null;
        try {
            usuarioOptional = usuarioService.buscarUsuarioPorId(idUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida.");
        }

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        usuarioService.apagarUsuario(usuarioOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuário " + usuarioOptional.get().getNome() + " apagado com sucesso.");
    }

    @DeleteMapping
    @Operation(summary = "Remoção da própria conta pelo usuário", method = "DELETE")
    @SecurityRequirement(name = "ecommerce")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Remoção de usuário realizado com sucesso",
                    content = @Content(examples = @ExampleObject("Usuário Heloisa Yasmin Costa apagado com sucesso."))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)})
    public ResponseEntity<Object> deletarUsuarioLogado() {

        UsuarioModel usuarioLogado = usuarioService.pegarUsuarioLogado();
        usuarioService.apagarUsuario(usuarioLogado);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario " + usuarioLogado.getNome() + " apagado com sucesso.");
    }


    @GetMapping
    @Operation(summary = "Exibe dados do usuário logado", method = "GET")
    @SecurityRequirement(name = "ecommerce")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exibição do usuário realizada com sucesso",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "{\n" +
                                    "  \"nome\": \"Bernardo Kauê da Silva\",\n" +
                                    "  \"login\": \"berna123\",\n" +
                                    "  \"cpf\": \"59105639042\",\n" +
                                    "  \"dataNasc\": \"16/01/1975\",\n" +
                                    "  \"sexo\": \"M\",\n" +
                                    "  \"telefone\": \"(45)12345-1234\",\n" +
                                    "  \"email\": \"bernardo-dasilva98@hotmail.co.jp\",\n" +
                                    "  \"cep\": \"80210-140\",\n" +
                                    "  \"uf\": \"PR\",\n" +
                                    "  \"cidade\": \"Curitiba\",\n" +
                                    "  \"rua\": \"Jardinete Rutílio de Sá Ribas\",\n" +
                                    "  \"numeroRua\": 252\n" +
                                    "}")})),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)})
    public ResponseEntity<UsuarioViewDTO> buscarUsuarioLogado() {
        UsuarioViewDTO usuarioViewDTO = usuarioService.mostrarUsuarioLogado(usuarioService.pegarUsuarioLogado());
        return ResponseEntity.status(HttpStatus.OK).body(usuarioViewDTO);
    }

    @GetMapping("/todos")
    @Operation(summary = "Busca os usuários", method = "GET")
    @SecurityRequirement(name = "ecommerce")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca do usuário realizada com sucesso",
                    content = @Content(mediaType = "application/json", examples = {@ExampleObject(
                            value = "{\n" +
                                    "  \"content\": [\n" +
                                    "    {\n" +
                                    "      \"id\": \"dceeba4a-b80c-42ac-8511-fae017a42bd5\",\n" +
                                    "      \"nome\": \"Andrea Simone Raquel Cardoso\",\n" +
                                    "      \"login\": \"andrea123\",\n" +
                                    "      \"senha\": \"$2a$10$1nsF8HaOjqB7lh3fgTA0wOow62WMWJsl6k5sZYMshihTDVFM/zuoq\",\n" +
                                    "      \"cpf\": \"20259721808\",\n" +
                                    "      \"dataNasc\": \"22/02/1970\",\n" +
                                    "      \"dataCadastro\": \"15/07/2023 às 18:34:59 UTC\",\n" +
                                    "      \"sexo\": \"F\",\n" +
                                    "      \"telefone\": \"(67)99156-1981\",\n" +
                                    "      \"email\": \"andrea_simone_cardoso@hp.com\",\n" +
                                    "      \"cep\": \"79604-030\",\n" +
                                    "      \"uf\": \"MS\",\n" +
                                    "      \"cidade\": \"Três Lagoas\",\n" +
                                    "      \"rua\": \"Rua João de Almeida Barros\",\n" +
                                    "      \"numeroRua\": 2884,\n" +
                                    "      \"enabled\": true,\n" +
                                    "      \"password\": \"$2a$10$1nsF8HaOjqB7lh3fgTA0wOow62WMWJsl6k5sZYMshihTDVFM/zuoq\",\n" +
                                    "      \"authorities\": [\n" +
                                    "        {\n" +
                                    "          \"id\": \"15124cfe-15bc-11ee-9769-94c6914cb639\",\n" +
                                    "          \"nome\": \"ROLE_USER\",\n" +
                                    "          \"authority\": \"ROLE_USER\"\n" +
                                    "        }\n" +
                                    "      ],\n" +
                                    "      \"username\": \"andrea123\",\n" +
                                    "      \"accountNonExpired\": true,\n" +
                                    "      \"credentialsNonExpired\": true,\n" +
                                    "      \"accountNonLocked\": true,\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/usuarios/id/dceeba4a-b80c-42ac-8511-fae017a42bd5\"\n" +
                                    "        }\n" +
                                    "      ]\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"id\": \"7e14f00c-ab42-4ad3-a052-091e996132a9\",\n" +
                                    "      \"nome\": \"Artur Ferraz\",\n" +
                                    "      \"login\": \"artur123\",\n" +
                                    "      \"senha\": \"$2a$10$0kAMvnI4u0rilAO2brHI1eULAll.lBVCXu1J2fTgTSI19GutNT1bW\",\n" +
                                    "      \"cpf\": \"68497813081\",\n" +
                                    "      \"dataNasc\": \"26/02/2002\",\n" +
                                    "      \"dataCadastro\": \"24/05/2023 às 12:50:05 UTC\",\n" +
                                    "      \"sexo\": \"F\",\n" +
                                    "      \"telefone\": \"(45)12345-1234\",\n" +
                                    "      \"email\": \"artur@pibnet.com.br\",\n" +
                                    "      \"cep\": \"63010-010\",\n" +
                                    "      \"uf\": \"CE\",\n" +
                                    "      \"cidade\": \"Juazeiro do Norte\",\n" +
                                    "      \"rua\": \"Rua São Pedro\",\n" +
                                    "      \"numeroRua\": 22,\n" +
                                    "      \"enabled\": true,\n" +
                                    "      \"password\": \"$2a$10$0kAMvnI4u0rilAO2brHI1eULAll.lBVCXu1J2fTgTSI19GutNT1bW\",\n" +
                                    "      \"authorities\": [\n" +
                                    "        {\n" +
                                    "          \"id\": \"15124cfe-15bc-11ee-9769-94c6914cb639\",\n" +
                                    "          \"nome\": \"ROLE_USER\",\n" +
                                    "          \"authority\": \"ROLE_USER\"\n" +
                                    "        }\n" +
                                    "      ],\n" +
                                    "      \"username\": \"artur123\",\n" +
                                    "      \"accountNonExpired\": true,\n" +
                                    "      \"credentialsNonExpired\": true,\n" +
                                    "      \"accountNonLocked\": true,\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/usuarios/id/7e14f00c-ab42-4ad3-a052-091e996132a9\"\n" +
                                    "        }\n" +
                                    "      ]\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"id\": \"836f431f-6e12-412e-a170-ac6d39081293\",\n" +
                                    "      \"nome\": \"Aurora Antonella Yasmin de Paula\",\n" +
                                    "      \"login\": \"aurora123\",\n" +
                                    "      \"senha\": \"$2a$10$R6YIwkfUj2HUO0a.kbaJXurLI8eoCHRy7c7OWoeR3HWqyQfriLLiC\",\n" +
                                    "      \"cpf\": \"87773610240\",\n" +
                                    "      \"dataNasc\": \"13/06/1963\",\n" +
                                    "      \"dataCadastro\": \"15/07/2023 às 18:25:54 UTC\",\n" +
                                    "      \"sexo\": \"F\",\n" +
                                    "      \"telefone\": \"(21)98620-6951\",\n" +
                                    "      \"email\": \"aurora_depaula@gustavoscoelho.com.br\",\n" +
                                    "      \"cep\": \"21725-270\",\n" +
                                    "      \"uf\": \"RJ\",\n" +
                                    "      \"cidade\": \"Rio de Janeiro\",\n" +
                                    "      \"rua\": \"Rua Sândalo\",\n" +
                                    "      \"numeroRua\": 981,\n" +
                                    "      \"enabled\": true,\n" +
                                    "      \"password\": \"$2a$10$R6YIwkfUj2HUO0a.kbaJXurLI8eoCHRy7c7OWoeR3HWqyQfriLLiC\",\n" +
                                    "      \"authorities\": [\n" +
                                    "        {\n" +
                                    "          \"id\": \"15124cfe-15bc-11ee-9769-94c6914cb639\",\n" +
                                    "          \"nome\": \"ROLE_USER\",\n" +
                                    "          \"authority\": \"ROLE_USER\"\n" +
                                    "        }\n" +
                                    "      ],\n" +
                                    "      \"username\": \"aurora123\",\n" +
                                    "      \"accountNonExpired\": true,\n" +
                                    "      \"credentialsNonExpired\": true,\n" +
                                    "      \"accountNonLocked\": true,\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/usuarios/id/836f431f-6e12-412e-a170-ac6d39081293\"\n" +
                                    "        }\n" +
                                    "      ]\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"id\": \"8f0ca30d-9066-4245-a962-4f0086926e3c\",\n" +
                                    "      \"nome\": \"Benedito Juan Felipe Sales\",\n" +
                                    "      \"login\": \"bened123\",\n" +
                                    "      \"senha\": \"$2a$10$K3UzNbc7TAdIiozqlOJHZe9leli3wefvke88JoFNvK/FSwNthDcAW\",\n" +
                                    "      \"cpf\": \"98153379470\",\n" +
                                    "      \"dataNasc\": \"30/11/2034\",\n" +
                                    "      \"dataCadastro\": \"09/07/2023 às 14:16:17 UTC\",\n" +
                                    "      \"sexo\": \"M\",\n" +
                                    "      \"telefone\": \"(69)98856-5123\",\n" +
                                    "      \"email\": \"benedito.juan.sales@gerj.com.br\",\n" +
                                    "      \"cep\": \"80210-140\",\n" +
                                    "      \"uf\": \"PR\",\n" +
                                    "      \"cidade\": \"Curitiba\",\n" +
                                    "      \"rua\": \"Jardinete Rutílio de Sá Ribas\",\n" +
                                    "      \"numeroRua\": 22,\n" +
                                    "      \"enabled\": true,\n" +
                                    "      \"password\": \"$2a$10$K3UzNbc7TAdIiozqlOJHZe9leli3wefvke88JoFNvK/FSwNthDcAW\",\n" +
                                    "      \"authorities\": [\n" +
                                    "        {\n" +
                                    "          \"id\": \"15124cfe-15bc-11ee-9769-94c6914cb639\",\n" +
                                    "          \"nome\": \"ROLE_USER\",\n" +
                                    "          \"authority\": \"ROLE_USER\"\n" +
                                    "        }\n" +
                                    "      ],\n" +
                                    "      \"username\": \"bened123\",\n" +
                                    "      \"accountNonExpired\": true,\n" +
                                    "      \"credentialsNonExpired\": true,\n" +
                                    "      \"accountNonLocked\": true,\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/usuarios/id/8f0ca30d-9066-4245-a962-4f0086926e3c\"\n" +
                                    "        }\n" +
                                    "      ]\n" +
                                    "    },\n" +
                                    "    {\n" +
                                    "      \"id\": \"2eb8d460-2ac3-48f0-bb2e-72cbd20c206c\",\n" +
                                    "      \"nome\": \"Bernardo Kauê da Silva\",\n" +
                                    "      \"login\": \"berna123\",\n" +
                                    "      \"senha\": \"$2a$10$0kAMvnI4u0rilAO2brHI1eULAll.lBVCXu1J2fTgTSI19GutNT1bW\",\n" +
                                    "      \"cpf\": \"59105639042\",\n" +
                                    "      \"dataNasc\": \"16/01/1975\",\n" +
                                    "      \"dataCadastro\": \"24/05/2023 às 12:51:44 UTC\",\n" +
                                    "      \"sexo\": \"M\",\n" +
                                    "      \"telefone\": \"(45)12345-1234\",\n" +
                                    "      \"email\": \"bernardo-dasilva98@hotmail.co.jp\",\n" +
                                    "      \"cep\": \"80210-140\",\n" +
                                    "      \"uf\": \"PR\",\n" +
                                    "      \"cidade\": \"Curitiba\",\n" +
                                    "      \"rua\": \"Jardinete Rutílio de Sá Ribas\",\n" +
                                    "      \"numeroRua\": 252,\n" +
                                    "      \"enabled\": true,\n" +
                                    "      \"password\": \"$2a$10$0kAMvnI4u0rilAO2brHI1eULAll.lBVCXu1J2fTgTSI19GutNT1bW\",\n" +
                                    "      \"authorities\": [\n" +
                                    "        {\n" +
                                    "          \"id\": \"10f90c00-15bc-11ee-9769-94c6914cb639\",\n" +
                                    "          \"nome\": \"ROLE_ADMIN\",\n" +
                                    "          \"authority\": \"ROLE_ADMIN\"\n" +
                                    "        },\n" +
                                    "        {\n" +
                                    "          \"id\": \"15124cfe-15bc-11ee-9769-94c6914cb639\",\n" +
                                    "          \"nome\": \"ROLE_USER\",\n" +
                                    "          \"authority\": \"ROLE_USER\"\n" +
                                    "        }\n" +
                                    "      ],\n" +
                                    "      \"username\": \"berna123\",\n" +
                                    "      \"accountNonExpired\": true,\n" +
                                    "      \"credentialsNonExpired\": true,\n" +
                                    "      \"accountNonLocked\": true,\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/usuarios/id/2eb8d460-2ac3-48f0-bb2e-72cbd20c206c\"\n" +
                                    "        }\n" +
                                    "      ]\n" +
                                    "    }\n" +
                                    "  ],\n" +
                                    "  \"pageable\": {\n" +
                                    "    \"sort\": {\n" +
                                    "      \"empty\": false,\n" +
                                    "      \"unsorted\": false,\n" +
                                    "      \"sorted\": true\n" +
                                    "    },\n" +
                                    "    \"offset\": 0,\n" +
                                    "    \"pageSize\": 5,\n" +
                                    "    \"pageNumber\": 0,\n" +
                                    "    \"unpaged\": false,\n" +
                                    "    \"paged\": true\n" +
                                    "  },\n" +
                                    "  \"last\": false,\n" +
                                    "  \"totalPages\": 3,\n" +
                                    "  \"totalElements\": 13,\n" +
                                    "  \"size\": 5,\n" +
                                    "  \"number\": 0,\n" +
                                    "  \"sort\": {\n" +
                                    "    \"empty\": false,\n" +
                                    "    \"unsorted\": false,\n" +
                                    "    \"sorted\": true\n" +
                                    "  },\n" +
                                    "  \"first\": true,\n" +
                                    "  \"numberOfElements\": 5,\n" +
                                    "  \"empty\": false\n" +
                                    "}")})),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content)
    })
    @Parameter(in = ParameterIn.QUERY, description = "Página", name = "page"
            , content = @Content(schema = @Schema(type = "integer", defaultValue = "0")))
    @Parameter(in = ParameterIn.QUERY, description = "Resultados por página", name = "size"
            , content = @Content(schema = @Schema(type = "integer", defaultValue = "5")))
    @Parameter(in = ParameterIn.QUERY, description = "Critério de ordenação. Padronizado pelo campo \"nome\" ascendente. É possível adicionar múltiplos critérios."
            , name = "sort")
    public ResponseEntity<Page<UsuarioModel>> buscarUsuarios(
            @ParameterObject
            @PageableDefault(page = 0, size = 5, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<UsuarioModel> listaUsuarios = usuarioService.buscarUsuarios(pageable);
        if (!listaUsuarios.isEmpty()) {
            for (UsuarioModel usuario : listaUsuarios) {
                usuario.add(linkTo(
                        methodOn(UsuarioController.class).buscarUsuarioPorId(usuario.getId().toString()))
                        .withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(listaUsuarios);
    }

    @GetMapping("/id/{id_usuario}")
    @Operation(summary = "Busca um usuário por ID", method = "GET")
    @SecurityRequirement(name = "ecommerce")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca do usuário realizado com sucesso",
                    content = @Content( examples = {@ExampleObject(
                            value = "{\n" +
                                    "  \"content\": [\n" +
                                    "    {\n" +
                                    "      \"id\": \"f94ea127-c9cd-4ac2-99cb-aa84ae84500b\",\n" +
                                    "      \"nome\": \"Heloisa Yasmin Costa\",\n" +
                                    "      \"login\": \"heloi123\",\n" +
                                    "      \"senha\": \"$2a$10$HhxGI2Ic8SkNNRU3vsq9m.3kJjzxRtik.uePu3WSjm3PxZeyS5VFC\",\n" +
                                    "      \"cpf\": \"70181125404\",\n" +
                                    "      \"dataNasc\": \"06/03/1945\",\n" +
                                    "      \"dataCadastro\": \"24/07/2023 às 16:58:49 UTC\",\n" +
                                    "      \"sexo\": \"F\",\n" +
                                    "      \"telefone\": \"(85)98981-2256\",\n" +
                                    "      \"email\": \"heloisa_costa@ciaimoveissjc.com\",\n" +
                                    "      \"cep\": \"61604-270\",\n" +
                                    "      \"uf\": \"CE\",\n" +
                                    "      \"cidade\": \"Caucaia\",\n" +
                                    "      \"rua\": \"Rua Sargento Feitosa\",\n" +
                                    "      \"numeroRua\": 981,\n" +
                                    "      \"enabled\": true,\n" +
                                    "      \"password\": \"$2a$10$HhxGI2Ic8SkNNRU3vsq9m.3kJjzxRtik.uePu3WSjm3PxZeyS5VFC\",\n" +
                                    "      \"authorities\": [\n" +
                                    "        {\n" +
                                    "          \"id\": \"15124cfe-15bc-11ee-9769-94c6914cb639\",\n" +
                                    "          \"nome\": \"ROLE_USER\",\n" +
                                    "          \"authority\": \"ROLE_USER\"\n" +
                                    "        }\n" +
                                    "      ],\n" +
                                    "      \"username\": \"heloi123\",\n" +
                                    "      \"accountNonExpired\": true,\n" +
                                    "      \"credentialsNonExpired\": true,\n" +
                                    "      \"accountNonLocked\": true,\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/usuarios/id/f94ea127-c9cd-4ac2-99cb-aa84ae84500b\"\n" +
                                    "        }\n" +
                                    "      ]\n" +
                                    "    }\n" +
                                    "  ],\n" +
                                    "  \"pageable\": {\n" +
                                    "    \"sort\": {\n" +
                                    "      \"empty\": false,\n" +
                                    "      \"unsorted\": false,\n" +
                                    "      \"sorted\": true\n" +
                                    "    },\n" +
                                    "    \"offset\": 0,\n" +
                                    "    \"pageSize\": 1,\n" +
                                    "    \"pageNumber\": 0,\n" +
                                    "    \"unpaged\": false,\n" +
                                    "    \"paged\": true\n" +
                                    "  },\n" +
                                    "  \"last\": true,\n" +
                                    "  \"totalPages\": 1,\n" +
                                    "  \"totalElements\": 1,\n" +
                                    "  \"size\": 1,\n" +
                                    "  \"number\": 0,\n" +
                                    "  \"sort\": {\n" +
                                    "    \"empty\": false,\n" +
                                    "    \"unsorted\": false,\n" +
                                    "    \"sorted\": true\n" +
                                    "  },\n" +
                                    "  \"first\": true,\n" +
                                    "  \"numberOfElements\": 1,\n" +
                                    "  \"empty\": false\n" +
                                    "}")})),
            @ApiResponse(responseCode = "400", description = "Id inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    public ResponseEntity<Object> buscarUsuarioPorId(
            @Parameter(content = @Content(examples = @ExampleObject("dceeba4a-b80c-42ac-8511-fae017a42bd5")))
            @PathVariable(value = "id_usuario") String idUsuario) {

        Optional<UsuarioModel> usuarioOptional = null;
        try {
            usuarioOptional = usuarioService.buscarUsuarioPorId(idUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida.");
        }

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        usuarioOptional.get().add(linkTo(
                methodOn(UsuarioController.class).buscarUsuarios(Pageable.unpaged()))
                .withRel("Lista de Usuários"));

        return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Busca os usuários por nome", method = "GET")
    @SecurityRequirement(name = "ecommerce")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca dos usuários realizada com sucesso",
                    content = @Content( examples = {@ExampleObject(
                            value = "{\n" +
                                    "  \"content\": [\n" +
                                    "    {\n" +
                                    "      \"id\": \"7e14f00c-ab42-4ad3-a052-091e996132a9\",\n" +
                                    "      \"nome\": \"Artur Ferraz\",\n" +
                                    "      \"login\": \"artur123\",\n" +
                                    "      \"senha\": \"$2a$10$0kAMvnI4u0rilAO2brHI1eULAll.lBVCXu1J2fTgTSI19GutNT1bW\",\n" +
                                    "      \"cpf\": \"68497813081\",\n" +
                                    "      \"dataNasc\": \"26/02/2002\",\n" +
                                    "      \"dataCadastro\": \"24/05/2023 às 12:50:05 UTC\",\n" +
                                    "      \"sexo\": \"F\",\n" +
                                    "      \"telefone\": \"(45)12345-1234\",\n" +
                                    "      \"email\": \"artur@pibnet.com.br\",\n" +
                                    "      \"cep\": \"63010-010\",\n" +
                                    "      \"uf\": \"CE\",\n" +
                                    "      \"cidade\": \"Juazeiro do Norte\",\n" +
                                    "      \"rua\": \"Rua São Pedro\",\n" +
                                    "      \"numeroRua\": 22,\n" +
                                    "      \"enabled\": true,\n" +
                                    "      \"password\": \"$2a$10$0kAMvnI4u0rilAO2brHI1eULAll.lBVCXu1J2fTgTSI19GutNT1bW\",\n" +
                                    "      \"accountNonExpired\": true,\n" +
                                    "      \"credentialsNonExpired\": true,\n" +
                                    "      \"accountNonLocked\": true,\n" +
                                    "      \"authorities\": [\n" +
                                    "        {\n" +
                                    "          \"id\": \"15124cfe-15bc-11ee-9769-94c6914cb639\",\n" +
                                    "          \"nome\": \"ROLE_USER\",\n" +
                                    "          \"authority\": \"ROLE_USER\"\n" +
                                    "        }\n" +
                                    "      ],\n" +
                                    "      \"username\": \"artur123\",\n" +
                                    "      \"links\": [\n" +
                                    "        {\n" +
                                    "          \"rel\": \"self\",\n" +
                                    "          \"href\": \"http://localhost:8080/usuarios/id/7e14f00c-ab42-4ad3-a052-091e996132a9\"\n" +
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
                                    "    \"pageNumber\": 0,\n" +
                                    "    \"pageSize\": 5,\n" +
                                    "    \"paged\": true,\n" +
                                    "    \"unpaged\": false\n" +
                                    "  },\n" +
                                    "  \"last\": true,\n" +
                                    "  \"totalElements\": 1,\n" +
                                    "  \"totalPages\": 1,\n" +
                                    "  \"size\": 5,\n" +
                                    "  \"number\": 0,\n" +
                                    "  \"sort\": {\n" +
                                    "    \"empty\": false,\n" +
                                    "    \"sorted\": true,\n" +
                                    "    \"unsorted\": false\n" +
                                    "  },\n" +
                                    "  \"first\": true,\n" +
                                    "  \"numberOfElements\": 1,\n" +
                                    "  \"empty\": false\n" +
                                    "}")})),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso proibido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nenhum Usuário encontrado", content = @Content)
    })
    @Parameter(in = ParameterIn.QUERY, description = "Página", name = "page"
            , content = @Content(schema = @Schema(type = "integer", defaultValue = "0")))
    @Parameter(in = ParameterIn.QUERY, description = "Resultados por página", name = "size"
            , content = @Content(schema = @Schema(type = "integer", defaultValue = "5")))
    @Parameter(in = ParameterIn.QUERY, description = "Critério de ordenação. Padronizado pelo campo \"nome\" ascendente. É possível adicionar múltiplos critérios."
            , name = "sort")
    public ResponseEntity<Object> buscarUsuariosPorNome(
            @Parameter(content = @Content(examples = @ExampleObject("Artur")))
            @PathVariable(value = "nome") String nome,
            @ParameterObject
            @PageableDefault(page = 0, size = 5, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {

        Optional<Page<UsuarioModel>> usuarioOptional = usuarioService.pesquisarUsuarios(nome, pageable);
        if (usuarioOptional.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário encontrado.");
        }

        for (UsuarioModel usuario : usuarioOptional.get()) {
            usuario.add(linkTo(
                    methodOn(UsuarioController.class).buscarUsuarioPorId(usuario.getId().toString()))
                    .withSelfRel());
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
    }
}
