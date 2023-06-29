package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.services.UsuarioService;
import com.ecommerceapi.utils.CEPUtils;
import com.ecommerceapi.utils.ControllerUtils;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


@RestController
@RequestMapping("/clientes")
public class UsuarioController {
    final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping
    public ResponseEntity<Object> cadastrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {

        List<Map<String, String>> listaErros = new ArrayList<>();
        if (errosDeValidacao.hasErrors()) {
            List<FieldError> listaFieldErros = errosDeValidacao.getFieldErrors();
            for (FieldError erro : listaFieldErros) {
                listaErros.add(ControllerUtils.adicionarErros(erro.getField(), erro.getDefaultMessage()));
            }
        }

        UsuarioModel usuarioModel = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDTO, usuarioModel);

        if (usuarioService.existsByLogin(usuarioModel.getLogin())) {
            listaErros.add(ControllerUtils.adicionarErros("login", "Login já utilizado."));
        }
        if (usuarioService.existsByCpf(usuarioModel.getCpf())) {
            listaErros.add(ControllerUtils.adicionarErros("cpf", "CPF já utilizado."));
        }
        if (usuarioService.existsByEmail(usuarioModel.getEmail())) {
            listaErros.add(ControllerUtils.adicionarErros("email", "Email já utilizado."));
        }

        try {
            usuarioModel = new CEPUtils().retornaCep(usuarioModel);
        } catch (RuntimeException e) {
            listaErros.add(ControllerUtils.adicionarErros("cep", "CEP não existe."));
        }

        try {
            usuarioModel.setData_nasc(LocalDate.parse(usuarioDTO.getData_nasc(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (DateTimeParseException e) {
            listaErros.add(ControllerUtils.adicionarErros("data_nasc", "Data inválida."));
        }

        if (!listaErros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listaErros);
        }

        usuarioModel.setNumero_rua(Integer.parseInt(usuarioDTO.getNumero_rua()));
        usuarioModel.setSexo(usuarioDTO.getSexo().charAt(0));
        usuarioModel.setData_cadastro(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvarUsuarios(usuarioModel));
    }


    @GetMapping
    public ResponseEntity<List<UsuarioModel>> buscarUsuarios() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarUsuarios());
    }

    @GetMapping("/id/{id_usuario}")
    public ResponseEntity<Object> buscarUsuarioPorId(@PathVariable(value = "id_usuario") String id_usuario) {

        UUID id = ControllerUtils.converteUUID(id_usuario);
        if (id_usuario.equals("") || id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");

        Optional<UsuarioModel> usuarioOptional = usuarioService.buscarUsuarioPorId(id);
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Object> buscarUsuariosPorNome(@PathVariable(value = "nome") String nome) {

        Optional<List<UsuarioModel>> usuarioOptional = usuarioService.pesquisarUsuarios(nome);

        if (usuarioOptional.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
    }

    @DeleteMapping("/{id_usuario}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable(value = "id_usuario") String id_usuario) {

        UUID id = ControllerUtils.converteUUID(id_usuario);
        if (id_usuario.equals("") || id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");

        Optional<UsuarioModel> usuarioOptional = usuarioService.buscarUsuarioPorId(id);
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
        }

        usuarioService.delete(usuarioOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuario apagado com sucesso.");
    }

    @PutMapping("/{id_usuario}")
    public ResponseEntity<Object> atualizarUsuario(@PathVariable(value = "id_usuario") String id_usuario,
                                                   @RequestBody @Valid UsuarioDTO usuarioDTO, BindingResult bindingResult) {

        UUID id = ControllerUtils.converteUUID(id_usuario);
        if (id_usuario.equals("") || id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id inválida");

        Optional<UsuarioModel> usuarioOptional = usuarioService.buscarUsuarioPorId(id);
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
        }

        List<Map<String, String>> listaErros = new ArrayList<>();
        List<String> listaNulos = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            var listaFieldErros = bindingResult.getFieldErrors();
            for (FieldError erro : listaFieldErros) {
                if (erro.getRejectedValue() == null) {
                    listaNulos.add(erro.getField());
                } else {
                    listaErros.add(ControllerUtils.adicionarErros(erro.getField(), erro.getDefaultMessage()));
                }
            }
        }

        UsuarioModel usuarioModel = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDTO, usuarioModel);
        usuarioModel.setId(id);
        usuarioModel.setData_cadastro(usuarioOptional.get().getData_cadastro());

        if (usuarioModel.getLogin() != null
                && !usuarioModel.getLogin().equals(usuarioOptional.get().getLogin())
                && usuarioService.existsByLogin(usuarioModel.getLogin())) {
            listaErros.add(ControllerUtils.adicionarErros("login", "Login já utilizado."));
        }
        if (usuarioModel.getCpf() != null
                && !usuarioModel.getCpf().equals(usuarioOptional.get().getCpf())
                && usuarioService.existsByCpf(usuarioModel.getCpf())) {
            listaErros.add(ControllerUtils.adicionarErros("cpf", "CPF já utilizado."));
        }
        if (usuarioModel.getEmail() != null
                && !usuarioModel.getEmail().equals(usuarioOptional.get().getEmail())
                && usuarioService.existsByEmail(usuarioModel.getEmail())) {
            listaErros.add(ControllerUtils.adicionarErros("email", "Email já utilizado."));
        }

        if (usuarioModel.getCep() != null) {
            try {
                usuarioModel = new CEPUtils().retornaCep(usuarioModel);
            } catch (RuntimeException e) {
                listaErros.add(ControllerUtils.adicionarErros("cep", "CEP não existe."));
            }
        }

        if (usuarioModel.getData_nasc() != null) {
            try {
                usuarioModel.setData_nasc(LocalDate.parse(usuarioDTO.getData_nasc(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } catch (DateTimeParseException e) {
                listaErros.add(ControllerUtils.adicionarErros("data_nasc", "Data inválida."));
            }
        }

        if (!listaErros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listaErros);
        }

        for (String campo : listaNulos) {
            switch (campo) {
                case "nome":
                    usuarioModel.setNome(usuarioOptional.get().getNome());
                    break;
                case "login":
                    usuarioModel.setLogin(usuarioOptional.get().getLogin());
                    break;
                case "senha":
                    usuarioModel.setSenha(usuarioOptional.get().getSenha());
                    break;
                case "cpf":
                    usuarioModel.setCpf(usuarioOptional.get().getCpf());
                    break;
                case "data_nasc":
                    usuarioModel.setData_nasc(usuarioOptional.get().getData_nasc());
                    break;
                case "sexo":
                    usuarioModel.setSexo(usuarioOptional.get().getSexo());
                    break;
                case "telefone":
                    usuarioModel.setTelefone(usuarioOptional.get().getTelefone());
                    break;
                case "email":
                    usuarioModel.setEmail(usuarioOptional.get().getEmail());
                    break;
                case "cep":
                    usuarioModel.setCep(usuarioOptional.get().getCep());
                    usuarioModel.setUf(usuarioOptional.get().getUf());
                    usuarioModel.setCidade(usuarioOptional.get().getCidade());
                    usuarioModel.setRua(usuarioOptional.get().getRua());
                    break;
                case "numero_rua":
                    usuarioModel.setNumero_rua(usuarioOptional.get().getNumero_rua());
                    break;
            }
        }

        if (usuarioDTO.getSexo() != null) {
            usuarioModel.setSexo(usuarioDTO.getSexo().charAt(0));
        }
        if (usuarioDTO.getNumero_rua() != null) {
            usuarioModel.setNumero_rua(Integer.parseInt(usuarioDTO.getNumero_rua()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.salvarUsuarios(usuarioModel));
    }
}
