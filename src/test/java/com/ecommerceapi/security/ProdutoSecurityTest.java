package com.ecommerceapi.security;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.mockedmodels.ProdutoDTOMock;
import com.ecommerceapi.mockedmodels.ProdutoModelMock;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ProdutoSecurityTest {

    @MockBean
    ProdutoService produtoService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private ProdutoDTOMock produtoDTO;
    private ProdutoModelMock produtoModel;
    private String idProduto, nome;
    Page<ProdutoModel> listaProdutos;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        produtoModel = new ProdutoModelMock();
        produtoDTO = new ProdutoDTOMock();
        idProduto = produtoModel.getId().toString();
        nome = produtoModel.getNome();
        listaProdutos = new PageImpl<>(Collections.singletonList(produtoModel));
    }


    // BuscarProdutos
    @Test
    @WithAnonymousUser
    void anonimoDeveBuscarProdutosStatusOk() throws Exception {
        when(produtoService.buscarProdutos(any(Pageable.class)))
                .thenReturn(listaProdutos);
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveBuscarProdutosStatusOk() throws Exception {
        when(produtoService.buscarProdutos(any(Pageable.class)))
                .thenReturn(listaProdutos);
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveBuscarProdutosStatusOk() throws Exception {
        when(produtoService.buscarProdutos(any(Pageable.class)))
                .thenReturn(listaProdutos);
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk());
    }

    // BuscarProdutosPorId
    @Test
    @WithAnonymousUser
    void anonimoDeveBuscarProdutoPorIdStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        mockMvc.perform(get("/produtos/id/" + idProduto))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveBuscarProdutoPorIdStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        mockMvc.perform(get("/produtos/id/" + idProduto))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveBuscarProdutoPorIdStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        mockMvc.perform(get("/produtos/id/" + idProduto))
                .andExpect(status().isOk());
    }

    // BuscarProdutosPorNome
    @Test
    @WithAnonymousUser
    void anonimoDeveBuscarProdutosPorNomeStatusOk() throws Exception {
        when(produtoService.pesquisarProdutos(anyString(), any(Pageable.class)))
                .thenReturn(Optional.of(listaProdutos));
        mockMvc.perform(get("/produtos/nome/" + nome))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveBuscarProdutosPorNomeStatusOk() throws Exception {
        when(produtoService.pesquisarProdutos(anyString(), any(Pageable.class)))
                .thenReturn(Optional.of(listaProdutos));
        mockMvc.perform(get("/produtos/nome/" + nome))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveBuscarProdutosPorNomeStatusOk() throws Exception {
        when(produtoService.pesquisarProdutos(anyString(), any(Pageable.class)))
                .thenReturn(Optional.of(listaProdutos));
        mockMvc.perform(get("/produtos/nome/" + nome))
                .andExpect(status().isOk());
    }

    // DeletarProdutos
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveDeletarProdutoStatusNaoAutorizado() throws Exception {
        mockMvc.perform(delete("/produtos/" + idProduto))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void usuarioNaoDeveDeletarProdutoStatusProibido() throws Exception {
        mockMvc.perform(delete("/produtos/" + idProduto))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminNaoDeveDeletarProdutoStatusNaoEncontrado() throws Exception {
        mockMvc.perform(delete("/produtos" + idProduto))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveDeletarProdutoStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.ofNullable(produtoModel));
        mockMvc.perform(delete("/produtos/" + idProduto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    // CadastrarProdutos
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveCadastrarProdutoStatusNaoAutorizado() throws Exception {
        mockMvc.perform(post("/produtos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNaoDeveCadastrarProdutoStatusProibido() throws Exception {
        mockMvc.perform(post("/produtos"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveCadastrarProdutoStatusCreated() throws Exception {

        when(produtoService.validaCadastroProduto(any(ProdutoDTO.class), any(BindingResult.class)))
                .thenReturn(produtoModel);
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(produtoDTO)))
                .andExpect(status().isCreated());
    }

    // AtualizarProdutos
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveAtualizarProdutoStatusNaoAutorizado() throws Exception {
        mockMvc.perform(put("/produtos/" + idProduto))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNaoDeveAtualizarProdutoStatusProibido() throws Exception {
        mockMvc.perform(put("/produtos/" + idProduto))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveAtualizarProdutoStatusOk() throws Exception {

        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        when(produtoService.validaAtualizacaoProduto(any(ProdutoModel.class), any(ProdutoDTO.class), any(BindingResult.class)))
                .thenReturn(produtoModel);
        mockMvc.perform(put("/produtos/" + idProduto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(produtoDTO)))
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
