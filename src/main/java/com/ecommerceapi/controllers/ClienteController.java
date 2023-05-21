package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.ClienteDTO;
import com.ecommerceapi.models.ClienteModel;
import com.ecommerceapi.services.ClienteService;
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
    public ResponseEntity<Object> salvarCliente(@RequestBody @Valid ClienteDTO clienteDTO, Errors errors) {

        List<Map<String, String>> listaErros = new ArrayList<>();
        if (errors.hasErrors()) {
            List<FieldError> listaFieldErros = errors.getFieldErrors();
            for (FieldError erro : listaFieldErros) {
                Map<String, String> msgErro = new HashMap<String, String>();
                msgErro.put("field", erro.getField());
                msgErro.put("defaultMessage", erro.getDefaultMessage());
                listaErros.add(msgErro);
            }
        }

        ClienteModel clienteModel = new ClienteModel();
        BeanUtils.copyProperties(clienteDTO, clienteModel);
        if (clienteService.existsByLogin(clienteModel.getLogin())) {
            Map<String, String> msgErro = new HashMap<String, String>();
            msgErro.put("field", "login");
            msgErro.put("defaultMessage", "Login já utilizado.");
            listaErros.add(msgErro);
        }
        if (clienteService.existsByCpf(clienteModel.getCpf())) {
            Map<String, String> msgErro = new HashMap<String, String>();
            msgErro.put("field", "cpf");
            msgErro.put("defaultMessage", "CPF já utilizado.");
            listaErros.add(msgErro);
        }
        try {
            clienteModel.setData_nasc(LocalDate.parse(clienteDTO.getData_nasc(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (DateTimeParseException e) {
            Map<String, String> msgErro = new HashMap<String, String>();
            msgErro.put("field", "data_nasc");
            msgErro.put("defaultMessage", "Data inválida");
            listaErros.add(msgErro);
        }

        if (!listaErros.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(listaErros);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.salvarCliente(clienteModel));
    }

    @GetMapping
    public List<ClienteModel> buscarClientes() {
        return clienteService.buscarClientes();
    }


}
