package com.ecommerceapi.services;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.repositories.ProdutoRepository;
import com.ecommerceapi.utils.ConversorUUID;
import com.ecommerceapi.utils.ManipuladorListaErros;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
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
    public ProdutoModel salvarProduto(ProdutoModel produtoModel) {
        return produtoRepository.save(produtoModel);
    }

    public ProdutoModel validaCadastroProduto(ProdutoDTO produtoDTO, BindingResult errosDeValidacao) {

        ManipuladorListaErros manipuladorListaErros = new ManipuladorListaErros(errosDeValidacao);

        ProdutoModel produtoModel = null;
        try {
            produtoModel = validaPorRegrasDeNegocio(manipuladorListaErros, produtoDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        produtoModel.setQuantidade_estoque(Integer.parseInt(produtoDTO.getQuantidade_estoque()));
        produtoModel.setPreco(BigDecimal.valueOf(
                Double.parseDouble(produtoDTO.getPreco().replace(',', '.'))));
        return produtoModel;
    }

    public ProdutoModel validaAtualizacaoProduto(ProdutoModel produtoSalvo, ProdutoDTO produtoDTO, BindingResult errosDeValidacao) {

        ManipuladorListaErros manipuladorListaErros = new ManipuladorListaErros(errosDeValidacao);

        if (produtoDTO.getNome() != null
                && produtoDTO.getNome().equals(produtoSalvo.getNome()))
            produtoDTO.setNome(null);

        ProdutoModel produtoAtualizado = null;
        try {
            produtoAtualizado = validaPorRegrasDeNegocio(manipuladorListaErros, produtoDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        if (produtoAtualizado.getNome() == null)
            produtoAtualizado.setNome(produtoSalvo.getNome());

        produtoAtualizado.setId(produtoSalvo.getId());
        produtoAtualizado.setQuantidade_estoque(Integer.parseInt(produtoDTO.getQuantidade_estoque()));
        produtoAtualizado.setPreco(BigDecimal.valueOf(
                Double.parseDouble(produtoDTO.getPreco().replace(',', '.'))));
        return produtoAtualizado;
    }

    private ProdutoModel validaPorRegrasDeNegocio(ManipuladorListaErros manipuladorListaErros, ProdutoDTO produtoDTO) {

        ProdutoModel produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDTO, produtoModel);

        if (produtoModel.getNome() != null
                && produtoRepository.existsByNome(produtoDTO.getNome())) {
            manipuladorListaErros.adicionarErros("nome", "Nome já utilizado.");
        }
        if (manipuladorListaErros.temErros()) {
            String erros = manipuladorListaErros.converteListaErrosParaStringJson();
            throw new IllegalArgumentException(erros);
        }
        return produtoModel;
    }

    @Transactional
    public void apagarProduto(ProdutoModel produtoModel) {
        produtoRepository.delete(produtoModel);
    }

    public void baixarEstoqueProduto(ProdutoModel produtoModel, int quantidade) {
        produtoModel.setQuantidade_estoque
                (produtoModel.getQuantidade_estoque() - quantidade);
        salvarProduto(produtoModel);
    }

    //Passado para private e apagado
//    private boolean existsByNome(String nome) {
//        return produtoRepository.existsByNome(nome);
//    }

    public Page<ProdutoModel> buscarProdutos(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    public Optional<ProdutoModel> buscarProdutoPorId(String id_produto) {
        UUID id = ConversorUUID.converteUUID(id_produto);
        if (id_produto.equals("") || id == null)
            throw new IllegalArgumentException();
        return produtoRepository.findById(id);
    }

    public Optional<Page<ProdutoModel>> pesquisarProdutos(String nome, Pageable pageable) {
        return produtoRepository.pesquisarProdutos(nome, pageable);
    }


}
