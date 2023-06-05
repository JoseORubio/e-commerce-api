package com.ecommerceapi.services;

import com.ecommerceapi.models.VendaModel;
import com.ecommerceapi.repositories.VendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendaService {

    final VendaRepository vendaRepository;

    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    @Transactional
    public VendaModel salvarVenda(VendaModel vendaModel){
        return vendaRepository.save(vendaModel);
    }
    @Transactional
    public void apagarVenda(VendaModel vendaModel){
        vendaRepository.delete(vendaModel);
    }

    public List<VendaModel> listarVendas(){
        return vendaRepository.findAll();
    }
}
