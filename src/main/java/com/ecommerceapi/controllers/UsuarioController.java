package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.dtos.UsuarioViewDTO;
import com.ecommerceapi.models.PapelDoUsuarioModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.services.PapelDoUsuarioService;
import com.ecommerceapi.services.PapelService;
import com.ecommerceapi.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

        UsuarioViewDTO usuarioViewDTO = usuarioService.mostrarUsuario(usuarioModel);
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
        UsuarioViewDTO usuarioViewDTO = usuarioService.mostrarUsuario(usuarioModel);
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
        UsuarioViewDTO usuarioViewDTO = usuarioService.mostrarUsuario(usuarioService.pegarUsuarioLogado());
        return ResponseEntity.status(HttpStatus.OK).body(usuarioViewDTO);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<UsuarioModel>> buscarUsuarios() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarUsuarios());
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

        return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Object> buscarUsuariosPorNome(@PathVariable(value = "nome") String nome) {

        Optional<List<UsuarioModel>> usuarioOptional = usuarioService.pesquisarUsuariosPorNome(nome);
        if (usuarioOptional.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário encontrado.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
    }
}
