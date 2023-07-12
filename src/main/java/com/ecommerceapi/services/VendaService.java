package com.ecommerceapi.services;

import com.ecommerceapi.models.*;
import com.ecommerceapi.repositories.VendaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Object efetivaVendaCompleta(UsuarioModel usuario, List<CarrinhoModel> carrinhoModel) {

        for (CarrinhoModel itens : carrinhoModel) {
            if (itens.getQuantidade() > itens.getProduto().getQuantidade_estoque()) {
                return String.format("Quantidade de " + itens.getProduto().getNome() + " indisponível no estoque.");
            }
        }

        VendaModel venda = salvarVenda(new VendaModel(usuario.getId()));
        for (CarrinhoModel itens : carrinhoModel) {
            ProdutoDaVendaModel produtoDaVendaModel =
                    new ProdutoDaVendaModel(venda.getId(),itens.getProduto().getId(),itens.getQuantidade());
            produtoDaVendaService.salvarProdutoVendido(produtoDaVendaModel);
            produtoService.baixarEstoqueProduto(itens.getProduto(), itens.getQuantidade());
        }
        return null;
    }

    @Transactional
    public void apagarVenda(VendaModel vendaModel) {
        vendaRepository.delete(vendaModel);
    }

    public List<VendaModel> listarVendas() {
        return vendaRepository.findAll();
    }
}
