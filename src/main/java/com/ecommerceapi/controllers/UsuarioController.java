package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.models.PapelDoUsuarioModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.services.PapelDoUsuarioService;
import com.ecommerceapi.services.PapelService;
import com.ecommerceapi.services.UsuarioService;
import com.ecommerceapi.utils.CEPUtils;
import com.ecommerceapi.utils.ValidacaoUtils;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
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

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioModel);
    }


//    @PostMapping
//    public ResponseEntity<Object> cadastrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {
//
//        List<Map<String, String>> listaErros = new ArrayList<>();
//        if (errosDeValidacao.hasErrors()) {
//            List<FieldError> listaFieldErros = errosDeValidacao.getFieldErrors();
//            for (FieldError erro : listaFieldErros) {
//                listaErros.add(ControllerUtils.adicionarErros(erro.getField(), erro.getDefaultMessage()));
//            }
//        }
//
//        UsuarioModel usuarioModel = new UsuarioModel();
//        BeanUtils.copyProperties(usuarioDTO, usuarioModel);
//
//        if (usuarioService.existsByLogin(usuarioModel.getLogin())) {
//            listaErros.add(ControllerUtils.adicionarErros("login", "Login já utilizado."));
//        }
//        if (usuarioService.existsByCpf(usuarioModel.getCpf())) {
//            listaErros.add(ControllerUtils.adicionarErros("cpf", "CPF já utilizado."));
//        }
//        if (usuarioService.existsByEmail(usuarioModel.getEmail())) {
//            listaErros.add(ControllerUtils.adicionarErros("email", "Email já utilizado."));
//        }
//
//        try {
//            usuarioModel = new CEPUtils().retornaCep(usuarioModel);
//        } catch (RuntimeException e) {
//            listaErros.add(ControllerUtils.adicionarErros("cep", "CEP não existe."));
//        }
//
//        try {
//            usuarioModel.setData_nasc(LocalDate.parse(usuarioDTO.getData_nasc()
//                    , DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)));
//        } catch ( DateTimeParseException e) {
//            listaErros.add(ControllerUtils.adicionarErros("data_nasc", "Data inválida."));
//        }
//
//        if (!listaErros.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listaErros);
//        }
//
//        usuarioModel.setNumero_rua(Integer.parseInt(usuarioDTO.getNumero_rua()));
//        usuarioModel.setSexo(usuarioDTO.getSexo().charAt(0));
//        usuarioModel.setData_cadastro(LocalDateTime.now(ZoneId.of("UTC")));
//        usuarioModel.setSenha(new BCryptPasswordEncoder().encode(usuarioModel.getSenha()));
//        usuarioService.salvarUsuarios(usuarioModel);
//        papelDoUsuarioService.salvarPapelDoUsuario(
//                new PapelDoUsuarioModel(usuarioModel.getId(),papelService.pegarIdPapelUsuario()));
//        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioModel);
//    }

    @GetMapping
    public ResponseEntity<UsuarioModel> buscarUsuarioLogado() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.pegarUsuarioLogado());
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioOptional.get());
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
        return ResponseEntity.status(HttpStatus.OK).body("Usuário "+usuarioOptional.get().getNome()+" apagado com sucesso.");
    }

    @DeleteMapping
    public ResponseEntity<Object> deletarUsuarioLogado() {

        UsuarioModel usuarioLogado = usuarioService.pegarUsuarioLogado();
        usuarioService.delete(usuarioLogado);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario " + usuarioLogado.getNome() + " apagado com sucesso.");
    }

    @PutMapping
    public ResponseEntity<Object> atualizarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO, BindingResult bindingResult) {

        UsuarioModel usuarioLogado = usuarioService.pegarUsuarioLogado();

        List<Map<String, String>> listaErros = new ArrayList<>();
        List<String> listaNulos = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            var listaFieldErros = bindingResult.getFieldErrors();
            for (FieldError erro : listaFieldErros) {
                if (erro.getRejectedValue() == null) {
                    listaNulos.add(erro.getField());
                } else {
                    listaErros.add(ValidacaoUtils.adicionarErros(erro.getField(), erro.getDefaultMessage()));
                }
            }
        }

        UsuarioModel usuarioModel = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDTO, usuarioModel);
        usuarioModel.setId(usuarioLogado.getId());
        usuarioModel.setData_cadastro(usuarioLogado.getData_cadastro());

//        if (usuarioModel.getLogin() != null
        if (usuarioModel.getLogin() != null
                && !usuarioModel.getLogin().equals(usuarioLogado.getLogin())
                && usuarioService.existsByLogin(usuarioModel.getLogin())) {
            listaErros.add(ValidacaoUtils.adicionarErros("login", "Login já utilizado."));
        }
        if (usuarioModel.getCpf() != null
                && !usuarioModel.getCpf().equals(usuarioLogado.getCpf())
                && usuarioService.existsByCpf(usuarioModel.getCpf())) {
            listaErros.add(ValidacaoUtils.adicionarErros("cpf", "CPF já utilizado."));
        }
        if (usuarioModel.getEmail() != null
                && !usuarioModel.getEmail().equals(usuarioLogado.getEmail())
                && usuarioService.existsByEmail(usuarioModel.getEmail())) {
            listaErros.add(ValidacaoUtils.adicionarErros("email", "Email já utilizado."));
        }

        if (usuarioModel.getCep() != null) {
            try {
                usuarioModel = new CEPUtils().retornaCep(usuarioModel);
            } catch (RuntimeException e) {
                listaErros.add(ValidacaoUtils.adicionarErros("cep", "CEP não existe."));
            }
        }

        if (usuarioDTO.getData_nasc() != null) {
            try {
                usuarioModel.setData_nasc(LocalDate.parse(usuarioDTO.getData_nasc()
                        , DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)));
            } catch (DateTimeParseException e) {
                listaErros.add(ValidacaoUtils.adicionarErros("data_nasc", "Data inválida."));
            }
        }

        if (!listaErros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listaErros);
        }

        for (String campo : listaNulos) {
            switch (campo) {
                case "nome":
                    usuarioModel.setNome(usuarioLogado.getNome());
                    break;
                case "login":
                    usuarioModel.setLogin(usuarioLogado.getLogin());
                    break;
                case "senha":
                    usuarioModel.setSenha(usuarioLogado.getSenha());
                    break;
                case "cpf":
                    usuarioModel.setCpf(usuarioLogado.getCpf());
                    break;
                case "data_nasc":
                    usuarioModel.setData_nasc(usuarioLogado.getData_nasc());
                    break;
                case "sexo":
                    usuarioModel.setSexo(usuarioLogado.getSexo());
                    break;
                case "telefone":
                    usuarioModel.setTelefone(usuarioLogado.getTelefone());
                    break;
                case "email":
                    usuarioModel.setEmail(usuarioLogado.getEmail());
                    break;
                case "cep":
                    usuarioModel.setCep(usuarioLogado.getCep());
                    usuarioModel.setUf(usuarioLogado.getUf());
                    usuarioModel.setCidade(usuarioLogado.getCidade());
                    usuarioModel.setRua(usuarioLogado.getRua());
                    break;
                case "numero_rua":
                    usuarioModel.setNumero_rua(usuarioLogado.getNumero_rua());
                    break;
            }
        }

        if (usuarioDTO.getSexo() != null) {
            usuarioModel.setSexo(usuarioDTO.getSexo().charAt(0));
        }
        if (usuarioDTO.getNumero_rua() != null) {
            usuarioModel.setNumero_rua(Integer.parseInt(usuarioDTO.getNumero_rua()));
        }

        usuarioService.salvarUsuario(usuarioModel);
        papelDoUsuarioService.salvarPapelDoUsuario(
                new PapelDoUsuarioModel(usuarioModel.getId(), papelService.pegarIdPapelUsuario()));
        return ResponseEntity.status(HttpStatus.OK).body(usuarioModel.getNome());
    }
}
