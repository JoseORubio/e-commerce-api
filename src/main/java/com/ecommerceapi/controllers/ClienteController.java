package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.ClienteDTO;
import com.ecommerceapi.models.ClienteModel;
import com.ecommerceapi.services.ClienteService;
import com.ecommerceapi.utils.CEPUtils;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/clientes")
public class ClienteController {
    final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    @PostMapping
    public ResponseEntity<Object> salvarCliente(@RequestBody @Valid ClienteDTO clienteDTO, Errors errosDeValidacao) {
        listaErros.clear();
        if (errosDeValidacao.hasErrors()) {
            List<FieldError> listaFieldErros = errosDeValidacao.getFieldErrors();
            for (FieldError erro : listaFieldErros) {
                adicionarErros(erro.getField(), erro.getDefaultMessage());
            }
        }

        ClienteModel clienteModel = new ClienteModel();
        BeanUtils.copyProperties(clienteDTO, clienteModel);

        if (clienteService.existsByLogin(clienteModel.getLogin())) {
            adicionarErros("login","Login já utilizado.");
        }
        if (clienteService.existsByCpf(clienteModel.getCpf())) {
            adicionarErros("cpf","CPF já utilizado.");
        }
        if (clienteService.existsByEmail(clienteModel.getEmail())) {
            adicionarErros("email","Email já utilizado.");
        }

        try {
            clienteModel = new CEPUtils().retornaCep(clienteModel);
        } catch (RuntimeException e){
            adicionarErros("cep","CEP não existe.");
        }

        try {
            clienteModel.setData_nasc(LocalDate.parse(clienteDTO.getData_nasc(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (DateTimeParseException e) {
            adicionarErros("data_nasc","Data inválida.");
        }

        if (!listaErros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(listaErros);
        }

        clienteModel.setNumero_rua(Integer.parseInt(clienteDTO.getNumero_rua()));
        clienteModel.setSexo(clienteDTO.getSexo().charAt(0));
        clienteModel.setData_cadastro(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.salvarCliente(clienteModel));
    }

    @GetMapping
    public List<ClienteModel> buscarClientes() {
        return clienteService.buscarClientes();
    }

    private List<Map<String, String>> listaErros = new ArrayList<>();
    private void adicionarErros(String campoErro, String msgErro){
        Map<String, String> mapErro = new HashMap<String, String>();
        mapErro.put("campo", campoErro);
        mapErro.put("mensagem", msgErro);
        listaErros.add(mapErro);
    }
}
