package com.ecommerceapi.services;

import com.ecommerceapi.models.ClienteModel;
import com.ecommerceapi.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteModel salvarCliente(ClienteModel clienteModel){
        return clienteRepository.save(clienteModel);
    }

    public List<ClienteModel> buscarClientes(){
        return clienteRepository.findAll();
    }

}
