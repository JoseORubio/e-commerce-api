package com.ecommerceapi.services;

import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.repositories.CarrinhoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<List<CarrinhoModel>> buscarCarrinhoDoUsuario(UsuarioModel usuario) {
        return carrinhoRepository.findByUsuario(usuario);
    }

    public Optional<CarrinhoModel> buscarProdutoDoUsuarioNoCarrinho(UsuarioModel usuario, ProdutoModel produto) {
        return carrinhoRepository.findByUsuarioAndProduto(usuario,produto);
    }

    @Transactional
    public void apagarItemCarrinho(CarrinhoModel carrinhoModel){
         carrinhoRepository.delete(carrinhoModel);
    }
}
