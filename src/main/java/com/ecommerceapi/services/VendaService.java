package com.ecommerceapi.services;

import com.ecommerceapi.models.*;
import com.ecommerceapi.repositories.VendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendaService {

    final VendaRepository vendaRepository;

    @Autowired
    ProdutoDaVendaService produtoDaVendaService;

    @Autowired
    ProdutoService produtoService;

    public VendaService(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    @Transactional
    public VendaModel salvarVenda(VendaModel vendaModel) {
        return vendaRepository.save(vendaModel);
    }

    public List<Object> efetivaVendaCompleta(UsuarioModel usuario, List<CarrinhoModel> carrinhoModel) {

        for (CarrinhoModel itens : carrinhoModel) {
            if (itens.getQuantidade() > itens.getProduto().getQuantidade_estoque()) {
                throw new IllegalArgumentException("Quantidade de " + itens.getProduto().getNome() + " indisponível no estoque.");
            }
        }
        List<Object> vendaComProdutos = new ArrayList<>();
        VendaModel venda = salvarVenda(new VendaModel(usuario.getId()));
        vendaComProdutos.add(venda);
        for (CarrinhoModel itens : carrinhoModel) {
            ProdutoDaVendaModel produtoDaVendaModel =
                    new ProdutoDaVendaModel(venda.getId(),itens.getProduto().getId(),itens.getQuantidade());
            vendaComProdutos.add(produtoDaVendaService.salvarProdutoVendido(produtoDaVendaModel));
            produtoService.baixarEstoqueProduto(itens.getProduto(), itens.getQuantidade());
        }
        return vendaComProdutos;
    }

    @Transactional
    public void apagarVenda(VendaModel vendaModel) {
        vendaRepository.delete(vendaModel);
    }

    public List<VendaModel> listarVendas() {
        return vendaRepository.findAll();
    }
}
