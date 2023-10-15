package com.ecommerceapi.controllers;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.mockedmodels.ProdutoDTOMock;
import com.ecommerceapi.mockedmodels.ProdutoModelMock;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {

    @InjectMocks
    ProdutoController produtoController;
    @Mock
    ProdutoService produtoService;

    @Autowired
    MockMvc mockMvc;

    ProdutoModel produtoModel;
    String idProduto, nomeProduto;
    ProdutoDTO produtoDTO;
    Page<ProdutoModel> listaProdutos;
    IllegalArgumentException exception;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .alwaysDo(print())
                .build();

        produtoModel = new ProdutoModelMock();
        idProduto = produtoModel.getId().toString();
        nomeProduto = produtoModel.getNome();
        produtoDTO = new ProdutoDTOMock();
        listaProdutos = new PageImpl<>(Collections.singletonList(produtoModel));

        exception = new IllegalArgumentException("[\n" +
                "  {\n" +
                "    \"Campo\": \"preco\",\n" +
                "    \"Erros\": \"Não deve estar em branco. Deve ser qualquer valor monetário até 9999,99 com dois dígitos após , ou .\"\n" +
                "  }]");
    }

    //CadastrarProdutos
    @Test
    public void deveCadastrarProdutosStatusCreated() throws Exception {
        when(produtoService.validaCadastroProduto(any(ProdutoDTO.class), any(BindingResult.class)))
                .thenReturn(produtoModel);
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(produtoDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void naoDeveCadastrarProdutosStatusBadRequest() throws Exception {
               when(produtoService.validaCadastroProduto(any(ProdutoDTO.class), any(BindingResult.class)))
                .thenThrow(exception);
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(produtoDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //AtualizarProdutos
    @Test
    public void deveAtualizarProdutosStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        when(produtoService.validaAtualizacaoProduto(any(ProdutoModel.class), any(ProdutoDTO.class), any(BindingResult.class)))
                .thenReturn(produtoModel);
        mockMvc.perform(put("/produtos/" + idProduto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(produtoDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void naoDeveAtualizarProdutosIdInvalidaStatusBadRequest() throws Exception {
        when(produtoService.buscarProdutoPorId("123"))
                .thenThrow(IllegalArgumentException.class);
        mockMvc.perform(put("/produtos/" + "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(produtoDTO)))
                .andExpect(content().string("Id inválida."))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void naoDeveAtualizarProdutosNaoEncontradoStatusNotFound() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.empty());
        mockMvc.perform(put("/produtos/" + idProduto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(produtoDTO)))
                .andExpect(content().string("Produto não encontrado."))
                .andExpect(status().isNotFound());
    }

    @Test
    public void naoDeveAtualizarProdutosDadosInvalidosStatusBadRequest() throws Exception {

        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        when(produtoService.validaAtualizacaoProduto(any(ProdutoModel.class), any(ProdutoDTO.class), any(BindingResult.class)))
                .thenThrow(exception);
        mockMvc.perform(put("/produtos/" + idProduto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(produtoDTO)))
                .andExpect(status().isBadRequest());
    }

    //RemoverProduto
    @Test
    void deveRemoverProdutoPorIdStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        mockMvc.perform(delete("/produtos/" + idProduto))
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveRemoverProdutoPorIdStatusBadRequest() throws Exception {
        when(produtoService.buscarProdutoPorId("123"))
                .thenThrow(IllegalArgumentException.class);
        mockMvc.perform(delete("/produtos/" + "123"))
                .andExpect(content().string("Id inválida."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveRemoverProdutoPorIdStatusNotFound() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.empty());
        mockMvc.perform(delete("/produtos/" + idProduto))
                .andExpect(content().string("Produto não encontrado."))
                .andExpect(status().isNotFound());
    }

    //BuscarProdutos
    @Test
    public void deveBuscarProdutosSemPageableStatusOk() throws Exception {

        when(produtoService.buscarProdutos(any(Pageable.class))).thenReturn(listaProdutos);
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk());
//        verifyNoMoreInteractions(produtoService);
    }

    @Test
    public void deveBuscarProdutosComPageableStatusOk() throws Exception {

        when(produtoService.buscarProdutos(any(Pageable.class))).thenReturn(listaProdutos);
        mockMvc.perform(get("/produtos?page=1&size=10"))
                .andExpect(status().isOk());
    }

    //BuscarProdutosPorId
    @Test
    void deveBuscarProdutoPorIdStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        mockMvc.perform(get("/produtos/id/" + idProduto))
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveBuscarProdutoPorIdStatusBadRequest() throws Exception {
        when(produtoService.buscarProdutoPorId("123"))
                .thenThrow(IllegalArgumentException.class);
        mockMvc.perform(get("/produtos/id/" + "123"))
                .andExpect(content().string("Id inválida."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveBuscarProdutoPorIdStatusNotFound() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/produtos/id/" + idProduto))
                .andExpect(content().string("Produto não encontrado."))
                .andExpect(status().isNotFound());
    }

    //BuscarProdutosPorNome
    @Test
    void deveBuscarProdutoPorNomeStatusOk() throws Exception {
        when(produtoService.pesquisarProdutos(anyString(), any(Pageable.class)))
                .thenReturn(Optional.of(listaProdutos));
        mockMvc.perform(get("/produtos/nome/" + nomeProduto))
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveBuscarProdutoPorNomeStatusNotFound() throws Exception {
        Page<ProdutoModel> listaProdutosVazia = new PageImpl<>(Collections.EMPTY_LIST);
        when(produtoService.pesquisarProdutos(anyString(), any(Pageable.class)))
                .thenReturn(Optional.ofNullable(listaProdutosVazia));
        mockMvc.perform(get("/produtos/nome/" + nomeProduto))
                .andExpect(content().string("Produto não encontrado."))
                .andExpect(status().isNotFound());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
