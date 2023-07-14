package com.ecommerceapi.services;

import com.ecommerceapi.controllers.ProdutoController;
import com.ecommerceapi.dtos.CarrinhoViewDTO;
import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.repositories.CarrinhoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

        if (quantidade > produtoModel.getQuantidade_estoque()) {
            return "Quantidade indisponível no estoque.";
        }

        Optional<CarrinhoModel> carrinhoExistente = buscarProdutoDoUsuarioNoCarrinho(usuarioModel, produtoModel);
        if (carrinhoExistente.isPresent()) {
                carrinhoExistente.get().setQuantidade(quantidade);
                return carrinhoExistente.get();
            }

        return new CarrinhoModel(usuarioModel, produtoModel, quantidade);
    }

    @Transactional
    public void apagarItemCarrinho(CarrinhoModel carrinhoModel) {
        carrinhoRepository.delete(carrinhoModel);
    }

    public Optional<List<CarrinhoModel>> buscarCarrinhoDoUsuario(UsuarioModel usuario) {
        return carrinhoRepository.findByUsuarioOrderByProdutoNome(usuario);
    }

    public Optional<CarrinhoModel> buscarProdutoDoUsuarioNoCarrinho(UsuarioModel usuario, ProdutoModel produto) {
        return carrinhoRepository.findByUsuarioAndProduto(usuario, produto);
    }

    public List<Object> gerarVisualizacaoCarrinho(List<CarrinhoModel> carrinho){

        BigDecimal valorTotalCarrinho = BigDecimal.ZERO;
        List<Object> listaCarrinhoView = new ArrayList<>();

        for (CarrinhoModel carrinhoModel : carrinho) {
            valorTotalCarrinho = valorTotalCarrinho.add(carrinhoModel.getValorTotalProduto());
            CarrinhoViewDTO carrinhoViewDTO = new CarrinhoViewDTO();
            BeanUtils.copyProperties(carrinhoModel, carrinhoViewDTO);
            carrinhoViewDTO.getProduto().add((linkTo(
                    methodOn(ProdutoController.class).buscarProdutoPorId(carrinhoViewDTO.getProduto().getId().toString()))
                    .withSelfRel()));
            listaCarrinhoView.add(carrinhoViewDTO);
        }

        Map<String, Object> infoItens = new LinkedHashMap<>();
        infoItens.put("Quantidade de itens", carrinho.size());
        infoItens.put("Valor total do carrinho", valorTotalCarrinho);
        listaCarrinhoView.add(infoItens);

        return listaCarrinhoView;
    }
}
