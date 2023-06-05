package com.ecommerceapi.services;

import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoService {
    @Autowired
    final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoModel salvarProduto(ProdutoModel produtoModel){
        return produtoRepository.save(produtoModel);
    }


    public void baixaProduto(ProdutoModel produtoModel){
        salvarProduto(produtoModel);
    }

    public boolean existsByNome(String nome){
        return produtoRepository.existsByNome(nome);
    }

    public List<ProdutoModel> buscarProdutos(){
        return produtoRepository.findByOrderByNome();
    }

    public Optional<ProdutoModel> buscarProdutoPorId(UUID id){
        return produtoRepository.findById(id);
    }

    public Optional<List<ProdutoModel>> pesquisarProdutos(String nome){
        return produtoRepository.pesquisarProdutos(nome);
    }

    @Transactional
    public void apagarProduto (ProdutoModel produtoModel){
        produtoRepository.delete(produtoModel);
    }


}
