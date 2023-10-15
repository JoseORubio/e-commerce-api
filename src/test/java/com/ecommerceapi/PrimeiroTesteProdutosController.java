package com.ecommerceapi;


import com.ecommerceapi.controllers.ProdutoController;
import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.services.ProdutoService;
import com.ecommerceapi.utils.ConversorUUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class PrimeiroTesteProdutosController {

    @InjectMocks
    ProdutoController produtoController;
    @Mock
    ProdutoService produtoService;
//    @Mock
//    Pageable pageable = mock(PageRequest.class);

    @Autowired
    MockMvc mockMvc;

    ProdutoModel produtoModel;
    ProdutoDTO produtoDTO;
    Page<ProdutoModel> listaProdutos;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .alwaysDo(print())
                .build();

        UUID uuid = ConversorUUID.converteUUID("b77a0ee4-3ec2-479b-9640-a215a6cab4a3");
        String nome = "Chinelo";
        int quantidade = 10;
        BigDecimal preco = BigDecimal.valueOf(15.4);

        produtoModel = new ProdutoModel();
        produtoModel.setId(uuid);
        produtoModel.setNome(nome);
        produtoModel.setQuantidade_estoque(quantidade);
        produtoModel.setPreco(preco);

        produtoDTO = new ProdutoDTO();
        produtoDTO.setNome(nome);
        produtoDTO.setQuantidade_estoque(String.valueOf(quantidade));
        produtoDTO.setPreco(String.valueOf(preco));


        listaProdutos = new PageImpl<>(Collections.singletonList(produtoModel));
    }

    @Test
    public void deveBuscarProdutosSemPageable() throws Exception {

        when(produtoService.buscarProdutos(any(Pageable.class))).thenReturn(listaProdutos);
        mockMvc.perform(get("/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        verify(produtoService).buscarProdutos(any(Pageable.class));
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void deveBuscarProdutoscomPageable() throws Exception {

        when(produtoService.buscarProdutos(any(Pageable.class))).thenReturn(listaProdutos);
//        mockMvc.perform(get("/produtos")
        mockMvc.perform(get("/produtos?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn()
        ;
        verify(produtoService).buscarProdutos(any(Pageable.class));
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void deveCadastrarProduto() throws Exception {

//        BindException bindingResult = new BindException(produtoDTO, "prod");
        when(produtoService.validaCadastroProduto(any(ProdutoDTO.class), any(BindingResult.class)))
                .thenReturn(produtoModel);

        mockMvc.perform(post("/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(produtoDTO))
                ).andExpect(MockMvcResultMatchers.status().isCreated())
//                .andReturn()
        ;

        verify(produtoService).salvarProduto(produtoModel);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void naoDeveCadastrarProduto() throws Exception {
        IllegalArgumentException exception = new IllegalArgumentException("[\n" +
                "  {\n" +
                "    \"Campo\": \"preco\",\n" +
                "    \"Erros\": \"Não deve estar em branco. Deve ser qualquer valor monetário até 9999,99 com dois dígitos após , ou .\"\n" +
                "  }]");


        when(produtoService.validaCadastroProduto(any(ProdutoDTO.class), any(BindingResult.class)))
                .thenThrow(exception);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(produtoDTO))
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void deveRemoverProduto() throws Exception {
        when(produtoService.buscarProdutoPorId("b77a0ee4-3ec2-479b-9640-a215a6cab4a3"))
                .thenReturn(Optional.of(produtoModel));

        mockMvc.perform(delete("/produtos/b77a0ee4-3ec2-479b-9640-a215a6cab4a3")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(produtoService).apagarProduto(produtoModel);
        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void naoDeveRemoverProdutoERetornarBadRequest() throws Exception {
        when(produtoService.buscarProdutoPorId("123123"))
                .thenThrow(IllegalArgumentException.class);

        mockMvc.perform(delete("/produtos/123123")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void naoDeveRemoverProdutoERetornarNotFound() throws Exception {
        when(produtoService.buscarProdutoPorId("b77a0ee4-3ec2-479b-9640-a215a6cab4a3"))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/produtos/b77a0ee4-3ec2-479b-9640-a215a6cab4a3")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        verifyNoMoreInteractions(produtoService);
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
