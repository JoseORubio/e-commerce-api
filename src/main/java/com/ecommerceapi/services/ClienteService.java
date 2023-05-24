package com.ecommerceapi.services;

import com.ecommerceapi.models.ClienteModel;
import com.ecommerceapi.repositories.ClienteRepository;
import com.ecommerceapi.utils.CEPUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {
    @Autowired
    final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteModel salvarCliente(ClienteModel clienteModel) {
        return clienteRepository.save(clienteModel);
    }

//    public List<ClienteModel> buscarClientes() {
//        return clienteRepository.findAll();
//    }

    public boolean existsByLogin(String login) {
        return clienteRepository.existsByLogin(login);
    }
    public boolean existsByCpf(String cpf){
        return clienteRepository.existsByCpf(cpf);
    }
    public boolean existsByEmail(String email){
        return clienteRepository.existsByEmail(email);
    }

    public List<ClienteModel> buscarClientes(){
        return clienteRepository.findByOrderByNome();
    }

    public Optional<ClienteModel> buscarClientePorId(UUID id){
        return clienteRepository.findById(id);
    }
}
