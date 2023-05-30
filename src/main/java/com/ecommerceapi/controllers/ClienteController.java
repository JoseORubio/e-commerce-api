package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.ClienteDTO;
import com.ecommerceapi.models.ClienteModel;
import com.ecommerceapi.services.ClienteService;
import com.ecommerceapi.utils.CEPUtils;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
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
public class ClienteController {
    final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @PostMapping
    public ResponseEntity<Object> cadastrarCliente(@RequestBody @Valid ClienteDTO clienteDTO, Errors errosDeValidacao) {
        List<Map<String, String>> listaErros = new ArrayList<>();
        if (errosDeValidacao.hasErrors()) {
            List<FieldError> listaFieldErros = errosDeValidacao.getFieldErrors();
            for (FieldError erro : listaFieldErros) {
                listaErros.add(adicionarErros(erro.getField(), erro.getDefaultMessage()));
            }
        }

        ClienteModel clienteModel = new ClienteModel();
        BeanUtils.copyProperties(clienteDTO, clienteModel);

        if (clienteService.existsByLogin(clienteModel.getLogin())) {
            listaErros.add(adicionarErros("login", "Login já utilizado."));
        }
        if (clienteService.existsByCpf(clienteModel.getCpf())) {
            listaErros.add(adicionarErros("cpf", "CPF já utilizado."));
        }
        if (clienteService.existsByEmail(clienteModel.getEmail())) {
            listaErros.add(adicionarErros("email", "Email já utilizado."));
        }

        try {
            clienteModel = new CEPUtils().retornaCep(clienteModel);
        } catch (RuntimeException e) {
            listaErros.add(adicionarErros("cep", "CEP não existe."));
        }

        try {
            clienteModel.setData_nasc(LocalDate.parse(clienteDTO.getData_nasc(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (DateTimeParseException e) {
            listaErros.add(adicionarErros("data_nasc", "Data inválida."));
        }

        if (!listaErros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(listaErros);
        }

        clienteModel.setNumero_rua(Integer.parseInt(clienteDTO.getNumero_rua()));
        clienteModel.setSexo(clienteDTO.getSexo().charAt(0));
        clienteModel.setData_cadastro(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.salvarCliente(clienteModel));
    }

    private Map<String, String> adicionarErros(String campoErro, String msgErro) {
        Map<String, String> mapErro = new HashMap<String, String>();
        mapErro.put("campo", campoErro);
        mapErro.put("mensagem", msgErro);
        return mapErro;
    }

    @GetMapping
    public ResponseEntity<List<ClienteModel>> buscarClientes() {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.buscarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarClientesPorId(@PathVariable(value = "id") String id) {
        UUID uuid = null;
        try {
             uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }

        Optional<ClienteModel> clienteOptional = clienteService.buscarClientePorId(uuid);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteOptional.get());
    }

    @GetMapping("/pesquisar/{nome}")
    public ResponseEntity<Object> pesquisarClientes(@PathVariable(value = "nome") String nome){
        Optional<List<ClienteModel>> clienteOptional = clienteService.pesquisarClientes(nome);
        if (clienteOptional.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarCliente(@PathVariable(value = "id") String id){
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }

        Optional<ClienteModel> clienteOptional = clienteService.buscarClientePorId(uuid);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }

        clienteService.delete(clienteOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Cliente apagado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarCliente(@PathVariable(value = "id") String id,
                                                 @RequestBody @Valid ClienteDTO clienteDTO, Errors errosDeValidacao){
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }

        Optional<ClienteModel> clienteOptional = clienteService.buscarClientePorId(uuid);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }

        List<Map<String, String>> listaErros = new ArrayList<>();
        if (errosDeValidacao.hasErrors()) {
            List<FieldError> listaFieldErros = errosDeValidacao.getFieldErrors();
            for (FieldError erro : listaFieldErros) {

                listaErros.add(adicionarErros(erro.getField(), erro.getDefaultMessage()));
            }
        }

        ClienteModel clienteModel = new ClienteModel();
        BeanUtils.copyProperties(clienteDTO, clienteModel);
        clienteModel.setId(uuid);
        clienteModel.setData_cadastro(clienteOptional.get().getData_cadastro());

        if (!clienteModel.getLogin().equals(clienteOptional.get().getLogin())
                && clienteService.existsByLogin(clienteModel.getLogin()) ) {
            listaErros.add(adicionarErros("login", "Login já utilizado."));
        }
        if (!clienteModel.getCpf().equals(clienteOptional.get().getCpf())
                && clienteService.existsByCpf(clienteModel.getCpf())) {
            listaErros.add(adicionarErros("cpf", "CPF já utilizado."));
        }
        if (!clienteModel.getEmail().equals(clienteOptional.get().getEmail())
                && clienteService.existsByEmail(clienteModel.getEmail())) {
            listaErros.add(adicionarErros("email", "Email já utilizado."));
        }

        try {
            clienteModel = new CEPUtils().retornaCep(clienteModel);
        } catch (RuntimeException e) {
            listaErros.add(adicionarErros("cep", "CEP não existe."));
        }

        try {
            clienteModel.setData_nasc(LocalDate.parse(clienteDTO.getData_nasc(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (DateTimeParseException e) {
            listaErros.add(adicionarErros("data_nasc", "Data inválida."));
        }

        if (!listaErros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(listaErros);
        }

        clienteModel.setNumero_rua(Integer.parseInt(clienteDTO.getNumero_rua()));
        clienteModel.setSexo(clienteDTO.getSexo().charAt(0));

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.salvarCliente(clienteModel));
    }

}
