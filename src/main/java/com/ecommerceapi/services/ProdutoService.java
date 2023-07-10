package com.ecommerceapi.services;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.repositories.ProdutoRepository;
import com.ecommerceapi.utils.ConversorUUID;
import com.ecommerceapi.utils.ValidatorUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
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
    public ProdutoModel salvarProduto(ProdutoModel produtoModel) {
        return produtoRepository.save(produtoModel);
    }

    public Object validaCadastroProduto(ProdutoDTO produtoDTO, BindingResult errosDeValidacao) {
        ValidatorUtils validatorUtils = new ValidatorUtils(errosDeValidacao);
        List<List<String>> listaErros = validatorUtils.getListaErros();

        Object validacaoProduto = validaPorRegrasDeNegocio(listaErros, produtoDTO);

        if (validacaoProduto instanceof List<?>) {
//            return (List<List<String>>) validacaoProduto;
            return ValidatorUtils.converteListaErrosParaMap((List<List<String>>) validacaoProduto);
        }

        ProdutoModel produtoModel = (ProdutoModel) validacaoProduto;
        produtoModel.setQuantidade_estoque(Integer.parseInt(produtoDTO.getQuantidade_estoque()));
        produtoModel.setPreco(BigDecimal.valueOf(
                Double.parseDouble(produtoDTO.getPreco().replace(',', '.'))));
        return produtoModel;
    }

    public Object validaAtualizacaoProduto(ProdutoModel produtoModel, ProdutoDTO produtoDTO, BindingResult errosDeValidacao) {
        ValidatorUtils validatorUtils = new ValidatorUtils(errosDeValidacao);
        List<List<String>> listaErros = validatorUtils.getListaErros();

        if (produtoDTO.getNome() != null
                && produtoDTO.getNome().equals(produtoModel.getNome()))
            produtoDTO.setNome(null);
        Object validacaoProduto = validaPorRegrasDeNegocio(listaErros,produtoDTO);
        if (validacaoProduto instanceof List<?>) {
            return ValidatorUtils.converteListaErrosParaMap((List<List<String>>) validacaoProduto);
        }

        ProdutoModel produtoValidadoModel = (ProdutoModel) validacaoProduto;

        if(produtoValidadoModel.getNome() == null)
            produtoValidadoModel.setNome(produtoModel.getNome());

        produtoValidadoModel.setId(produtoModel.getId());
        produtoValidadoModel.setQuantidade_estoque(Integer.parseInt(produtoDTO.getQuantidade_estoque()));
        produtoValidadoModel.setPreco(BigDecimal.valueOf(
                Double.parseDouble(produtoDTO.getPreco().replace(',', '.'))));
        return produtoValidadoModel;
    }

    private Object validaPorRegrasDeNegocio(List<List<String>> listaErros, ProdutoDTO produtoDTO) {
        ValidatorUtils validatorUtils = new ValidatorUtils(listaErros);
        ProdutoModel produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDTO, produtoModel);

        if (produtoModel.getNome() != null
                && existsByNome(produtoDTO.getNome())) {
            validatorUtils.adicionarErros("nome", "Nome já utilizado.");
        }
        if (!validatorUtils.getListaErros().isEmpty()) return validatorUtils.getListaErros();
        return produtoModel;
    }

    public boolean existsByNome(String nome) {
        return produtoRepository.existsByNome(nome);
    }

    public List<ProdutoModel> buscarProdutos() {
        return produtoRepository.findByOrderByNome();
    }

    public Optional<ProdutoModel> buscarProdutoPorId(String id_produto) {
        UUID id = ConversorUUID.converteUUID(id_produto);
        if (id_produto.equals("") || id == null)
            throw new RuntimeException();
        return produtoRepository.findById(id);
    }

    public Optional<List<ProdutoModel>> pesquisarProdutos(String nome) {
        return produtoRepository.pesquisarProdutos(nome);
    }

    @Transactional
    public void apagarProduto(ProdutoModel produtoModel) {
        produtoRepository.delete(produtoModel);
    }


}
