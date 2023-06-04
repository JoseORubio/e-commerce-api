package com.ecommerceapi.services;

import com.ecommerceapi.models.ProdutoDaVendaModel;
import com.ecommerceapi.repositories.ProdutoDaVendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProdutoDaVendaService {
    final ProdutoDaVendaRepository produtoDaVendaRepository;

    public ProdutoDaVendaService(ProdutoDaVendaRepository produtoDaVendaRepository) {
        this.produtoDaVendaRepository = produtoDaVendaRepository;
    }

    @Transactional
    public ProdutoDaVendaModel inserirProduto(ProdutoDaVendaModel produtoDaVendaModel){
        return  produtoDaVendaRepository.save(produtoDaVendaModel);
    }
}