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
    public ProdutoDaVendaModel salvarProdutoVendido(ProdutoDaVendaModel produtoDaVendaModel){
        return  produtoDaVendaRepository.save(produtoDaVendaModel);
    }
    @Transactional
    public void apagarProdutoVendido(ProdutoDaVendaModel produtoDaVendaModel){
         produtoDaVendaRepository.delete(produtoDaVendaModel);
    }
}
