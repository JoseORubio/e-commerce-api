package com.ecommerceapi.security;

import com.ecommerceapi.mockedmodels.ProdutoModelMock;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ProdutoSecurityTest {

    @MockBean
    ProdutoService produtoService;
//    @MockBean
//    VendaService vendaService;
//    @MockBean
//    CarrinhoService carrinhoService;
//    @MockBean
//    PapelService papelService;
//    @MockBean
//    PapelDoUsuarioService papelDoUsuarioService;
//    @MockBean
//    ProdutoDaVendaService produtoDaVendaService;
//    @MockBean
//    UsuarioService usuarioService;


    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private ProdutoModelMock produtoModel;
    private String id,nome;
    Page<ProdutoModel> listaProdutos;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        produtoModel = new ProdutoModelMock();
        id = produtoModel.getId().toString();
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
        when(produtoService.buscarProdutoPorId(id))
                .thenReturn(Optional.of(produtoModel));
        mockMvc.perform(get("/produtos/id/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveBuscarProdutoPorIdStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(id))
                .thenReturn(Optional.of(produtoModel));
        mockMvc.perform(get("/produtos/id/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveBuscarProdutoPorIdStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(id))
                .thenReturn(Optional.of(produtoModel));
        mockMvc.perform(get("/produtos/id/" + id))
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
        mockMvc.perform(delete("/produtos/" + id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void usuarioNaoDeveDeletarProdutoStatusProibido() throws Exception {
        mockMvc.perform(delete("/produtos/" + id))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminNaoDeveDeletarProdutoStatusNaoEncontrado() throws Exception {
        mockMvc.perform(delete("/produtos" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveDeletarProdutoStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(id))
                .thenReturn(Optional.ofNullable(produtoModel));
        mockMvc.perform(delete("/produtos/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
