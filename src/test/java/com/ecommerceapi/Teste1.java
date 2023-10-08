package com.ecommerceapi;

//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.repositories.ProdutoRepository;
import com.ecommerceapi.services.ProdutoService;
import com.ecommerceapi.utils.CEPUtils;
import com.ecommerceapi.utils.ConversorUUID;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Teste1 {

    @Test
    void testaUUID() {
//        ConversorUUID conversorUUID = new ConversorUUID();
        String id = UUID.randomUUID().toString();
        UUID uuid = ConversorUUID.converteUUID(id);
        assertEquals(uuid.toString(), id);

    }

    @InjectMocks
    ProdutoService produtoService;

    @Mock
    ProdutoRepository produtoRepository;

    ProdutoModel produto;

    @Test
    void buscaProdutoPorId() {
        when(produtoRepository.findById(produto.getId()))
                .thenReturn(Optional.ofNullable(produto));

        Optional<ProdutoModel> produtoBuscado =
                produtoService.buscarProdutoPorId("b77a0ee4-3ec2-479b-9640-a215a6cab4a3");

        assertEquals(Optional.ofNullable(produto), produtoBuscado);

    }

    @Test
    void naoBuscaProdutoPorIdComParametroNulo() {

        final RuntimeException exception =
                assertThrows(RuntimeException.class,
                        () -> {
                            produtoService.buscarProdutoPorId(null);
                        });

//        assertThat(exception,isA(RuntimeException.class));
        assertThat(exception, notNullValue());
//        assertThat(exception.getCause(), nullValue() );
        verifyNoInteractions(produtoRepository);
    }

    @Test
    void buscaProduto() {

        Pageable pageable = Pageable.unpaged();
        Page<ProdutoModel> produtoModelPage = new PageImpl<>(Collections.singletonList(produto));

        when(produtoRepository.findAll(pageable)).thenReturn(produtoModelPage);

        Page<ProdutoModel> produtos = produtoService.buscarProdutos(pageable);

        assertEquals("Chinelo", produtos.get().toList().get(0).getNome());
//        produtos.get().toList().get(0).getNome();
//        produtos.get().toList().get(0);

//        System.out.println(produtos.get().toList().get(0));

    }

    @Test
    void deveBaixarEstoqueProduto() {

        when(produtoRepository.save(produto)).thenReturn(produto);

        int quantidade = 3;
        produtoService.baixarEstoqueProduto(produto, quantidade);

        assertEquals(produto.getQuantidade_estoque(), 7);

    }

    @Test
    void deveValidarCadastroProduto() {
//        BindingResult bindingResult = mock(BindingResult.class);
//            when(bindingResult.hasErrors()).thenReturn(false);
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Chinelo");
        produtoDTO.setPreco("15.4");
        produtoDTO.setQuantidade_estoque("10");


        BindException bindingResult = new BindException(produtoDTO, "prod");
        FieldError fieldError = new FieldError("prod", "nome", "Nome invalido");
        bindingResult.addError(fieldError);

        assertThrows(IllegalArgumentException.class,
                () -> {
                    produtoService.validaCadastroProduto(produtoDTO, bindingResult);
                });

//        ProdutoModel produtoValidado = produtoService.validaCadastroProduto(produtoDTO,bindingResult);
//
//        assertEquals(produtoValidado.getNome(), produto.getNome());
//        assertEquals(produtoValidado.getPreco(), produto.getPreco());
//        assertEquals(produtoValidado.getQuantidade_estoque(), produto.getQuantidade_estoque());


    }

    @Test
    void naoDeveValidarCadastroProduto() {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Chinelo");
        produtoDTO.setPreco("15.4");
        produtoDTO.setQuantidade_estoque("10");

        BindException bindingResult = new BindException(produtoDTO, "prod");
        FieldError fieldError = new FieldError("prod", "nome", "Nome invalido");
        bindingResult.addError(fieldError);

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    produtoService.validaCadastroProduto(produtoDTO, bindingResult);
                });

//        List<FieldError> listaFieldErros = bindingResult.getFieldErrors();
//        for (FieldError erro : listaFieldErros) {
//            System.out.println(erro.getField() + "   " + erro.getDefaultMessage());
//        }

        assertThat(exception, notNullValue());
        verify(produtoRepository).existsByNome(produtoDTO.getNome());
        verifyNoMoreInteractions(produtoRepository);
    }

    @BeforeEach
    public void setup() {
        UUID uuid = ConversorUUID.converteUUID("b77a0ee4-3ec2-479b-9640-a215a6cab4a3");
        String nome = "Chinelo";
        int quantidade = 10;
        BigDecimal preco = BigDecimal.valueOf(15.4);
        produto = new ProdutoModel();
        produto.setId(uuid);
        produto.setNome(nome);
        produto.setQuantidade_estoque(quantidade);
        produto.setPreco(preco);


    }


}
