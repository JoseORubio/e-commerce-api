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
@RequestMapping ("/clientes")
public class ClienteController {
    final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Object> salvarCliente(@RequestBody @Valid ClienteDTO clienteDTO, Errors errors){
    if (errors.hasErrors()){
        List<Map<String, String>> listaErros = new ArrayList<>();

        List<FieldError> listaFieldErros = errors.getFieldErrors();
        for (FieldError erro: listaFieldErros) {
            Map<String, String> msgErro = new HashMap<String, String>();
            msgErro.put("field", erro.getField());
            msgErro.put("defaultMessage", erro.getDefaultMessage());
            listaErros.add(msgErro);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(listaErros);
    }

        ClienteModel clienteModel = new ClienteModel();
        BeanUtils.copyProperties(clienteDTO,clienteModel);
        try {
            clienteModel.setData_nasc(LocalDate.parse(
                    clienteDTO.getData_nasc(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch(DateTimeParseException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data inválida");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.salvarCliente(clienteModel));
    }

    @GetMapping
    public List<ClienteModel> buscarClientes(){
        return clienteService.buscarClientes();
    }


}
