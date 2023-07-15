package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.dtos.UsuarioViewDTO;
import com.ecommerceapi.models.PapelDoUsuarioModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.services.PapelDoUsuarioService;
import com.ecommerceapi.services.PapelService;
import com.ecommerceapi.services.UsuarioService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Object> cadastrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {

        Object validador = usuarioService.validaCadastroUsuario(usuarioDTO, errosDeValidacao);

        if (validador instanceof List<?>) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validador);
        }

        UsuarioModel usuarioModel = (UsuarioModel) validador;
        usuarioService.salvarUsuario(usuarioModel);
        papelDoUsuarioService.salvarPapelDoUsuario(
                new PapelDoUsuarioModel(usuarioModel.getId(), papelService.pegarIdPapelUsuario()));

        UsuarioViewDTO usuarioViewDTO = usuarioService.mostrarUsuarioLogado(usuarioModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioViewDTO);
    }

    @PutMapping
    public ResponseEntity<Object> atualizarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {

        UsuarioModel usuarioLogado = usuarioService.pegarUsuarioLogado();

        Object validador = usuarioService.validaAtualizacaoUsuario(usuarioLogado, usuarioDTO, errosDeValidacao);

        if (validador instanceof List<?>) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validador);
        }

        UsuarioModel usuarioModel = (UsuarioModel) validador;
        usuarioService.salvarUsuario(usuarioModel);
        papelDoUsuarioService.salvarPapelDoUsuario(
                new PapelDoUsuarioModel(usuarioModel.getId(), papelService.pegarIdPapelUsuario()));
        UsuarioViewDTO usuarioViewDTO = usuarioService.mostrarUsuarioLogado(usuarioModel);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioViewDTO);
    }


    @DeleteMapping("/{id_usuario}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable(value = "id_usuario") String id_usuario) {

        Optional<UsuarioModel> usuarioOptional = null;
        try {
            usuarioOptional = usuarioService.buscarUsuarioPorId(id_usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id inválida.");
        }

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        usuarioService.delete(usuarioOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuário " + usuarioOptional.get().getNome() + " apagado com sucesso.");
    }

    @DeleteMapping
    public ResponseEntity<Object> deletarUsuarioLogado() {

        UsuarioModel usuarioLogado = usuarioService.pegarUsuarioLogado();
        usuarioService.delete(usuarioLogado);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario " + usuarioLogado.getNome() + " apagado com sucesso.");
    }


    @GetMapping
    public ResponseEntity<UsuarioViewDTO> buscarUsuarioLogado() {
        UsuarioViewDTO usuarioViewDTO = usuarioService.mostrarUsuarioLogado(usuarioService.pegarUsuarioLogado());
        return ResponseEntity.status(HttpStatus.OK).body(usuarioViewDTO);
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<UsuarioModel>> buscarUsuarios(
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
    public ResponseEntity<Object> buscarUsuarioPorId(@PathVariable(value = "id_usuario") String id_usuario) {

        Optional<UsuarioModel> usuarioOptional = null;
        try {
            usuarioOptional = usuarioService.buscarUsuarioPorId(id_usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id inválida.");
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
    public ResponseEntity<Object> buscarUsuariosPorNome(
            @PathVariable(value = "nome") String nome,
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
