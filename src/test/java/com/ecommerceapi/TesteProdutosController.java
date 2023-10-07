package com.ecommerceapi;


import com.ecommerceapi.controllers.ProdutoController;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.repositories.ProdutoRepository;
import com.ecommerceapi.services.ProdutoService;
import com.ecommerceapi.utils.ConversorUUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
public class TesteProdutosController {

    @InjectMocks
    ProdutoController produtoController;

    @Mock
    ProdutoService produtoService;
    @Mock
    Pageable pageable = mock(PageRequest.class);


    MockMvc mockMvc;

    ProdutoModel produtoModel;

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


        listaProdutos = new PageImpl<>(Collections.singletonList(produtoModel));
    }

    @Test
    public void deveBuscarProdutosComentado() throws Exception {

        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        Page<ProdutoModel> produtosPage = new PageImpl<>(List.of(produtoModel), pageable, 0);
//        Page<TournamentEntity> tournamentEntitiesPage = new PageImpl<>(List.of(obj1, obj2), pageable, 0);


//        PageRequest pageRequest = mock(PageRequest.class);
//        when(produtoService.buscarProdutos(pageable)).thenReturn(produtosPage);

//        when(produtoService.buscarProdutos(Pageable.unpaged())).thenReturn(listaProdutos);
//        when(produtoService.buscarProdutos(Mockito.any(PageRequest.class))).thenReturn(listaProdutos);
        when(produtoService.buscarProdutos(null)).thenReturn(listaProdutos);
//        when(produtoService.buscarProdutos(pageRequest)).thenReturn(listaProdutos);

//        mockMvc.perform(get("/produtos")
        mockMvc.perform(get("/produtos")
//        mockMvc.perform(get("/produtos?page=1&size=10")
                                .contentType(MediaType.APPLICATION_JSON)
//                .param("pageable", produtosPage.toString())
//                .param("pageable", pageRequest.toString())
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

//        verify(produtoService).buscarProdutos(Pageable.unpaged());
//        verifyNoMoreInteractions(produtoService);
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
        mockMvc.perform(get("/produtos?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        verify(produtoService).buscarProdutos(any(Pageable.class));
        verifyNoMoreInteractions(produtoService);
    }


}
