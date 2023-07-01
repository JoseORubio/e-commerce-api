package com.ecommerceapi.services;

import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.repositories.CarrinhoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoService {
    final CarrinhoRepository carrinhoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository) {
        this.carrinhoRepository = carrinhoRepository;
    }

    @Transactional
    public CarrinhoModel inserirCarrinho(CarrinhoModel carrinhoModel){
        return  carrinhoRepository.save(carrinhoModel);
    }
}
