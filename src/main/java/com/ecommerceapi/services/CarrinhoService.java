package com.ecommerceapi.services;

import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.repositories.CarrinhoRepository;
import com.ecommerceapi.utils.ValidatorUtils;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Service
public class CarrinhoService {
    final CarrinhoRepository carrinhoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository) {
        this.carrinhoRepository = carrinhoRepository;
    }

    @Transactional
    public CarrinhoModel inserirCarrinho(CarrinhoModel carrinhoModel) {
        return carrinhoRepository.save(carrinhoModel);
    }

    public Object validaInsercaoProduto(UsuarioModel usuarioModel, ProdutoModel produtoModel, String quantidadeString) {
        Object validacaoCarrinho = validaPorRegrasDeNegocio(usuarioModel, produtoModel, quantidadeString);

        if (validacaoCarrinho instanceof String) {
            return validacaoCarrinho;
        }
        CarrinhoModel carrinho = (CarrinhoModel) validacaoCarrinho;
        return carrinho;
    }

    private Object validaPorRegrasDeNegocio(UsuarioModel usuarioModel, ProdutoModel produtoModel, String quantidadeString) {


        Integer quantidade = null;
        try {
            quantidade = Integer.parseInt(quantidadeString);
            if (quantidade < 1) {
                return "Quantidade inválida.";
            }
        } catch (NumberFormatException e) {
            return "Quantidade inválida.";
        }
        Optional<CarrinhoModel> carrinhoExistente = buscarProdutoDoUsuarioNoCarrinho(usuarioModel, produtoModel);

        if (carrinhoExistente.isPresent()) {
            if (carrinhoExistente.get().getQuantidade() + quantidade > produtoModel.getQuantidade_estoque()) {
                return "Quantidade indisponível no estoque.";
            } else {
                carrinhoExistente.get().setQuantidade(carrinhoExistente.get().getQuantidade() + quantidade);
                return carrinhoExistente.get();
            }
        }
        if (quantidade > produtoModel.getQuantidade_estoque()) {
            return "Quantidade indisponível no estoque.";
        }
        return new CarrinhoModel(usuarioModel, produtoModel, quantidade);
    }

    public Optional<List<CarrinhoModel>> buscarCarrinhoDoUsuario(UsuarioModel usuario) {
        return carrinhoRepository.findByUsuario(usuario);
    }

    public Optional<CarrinhoModel> buscarProdutoDoUsuarioNoCarrinho(UsuarioModel usuario, ProdutoModel produto) {
        return carrinhoRepository.findByUsuarioAndProduto(usuario, produto);
    }

    @Transactional
    public void apagarItemCarrinho(CarrinhoModel carrinhoModel) {
        carrinhoRepository.delete(carrinhoModel);
    }
}
