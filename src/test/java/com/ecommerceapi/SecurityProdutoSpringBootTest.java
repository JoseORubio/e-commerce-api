package com.ecommerceapi;

import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.services.*;
import com.ecommerceapi.utils.ConversorUUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SecurityProdutoSpringBootTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    ProdutoService produtoService;
    @MockBean
    VendaService vendaService;
    @MockBean
    CarrinhoService carrinhoService;
    @MockBean
    PapelService papelService;
    @MockBean
    PapelDoUsuarioService papelDoUsuarioService;
    @MockBean
    ProdutoDaVendaService produtoDaVendaService;
    @MockBean
    UsuarioService usuarioService;


    ProdutoModel produtoModel;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
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
    }

    @Test
//    @WithMockUser(value = "spring")
//    @WithMockUser(username = "asd", authorities = "ADMIN")
//    @WithMockUser(username="admin",roles = "ADMIN")
//    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    @WithAnonymousUser
     void naoDeletarProdutoERetornarNaoAutorizado() throws Exception {
        mockMvc.perform(delete("/produtos/123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
     void naoDeletarProdutoERetornarNaoEncontrado() throws Exception {
        mockMvc.perform(delete("/produtos/123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin",roles = "USER")
     void naoDeletarProdutoERetornarAcessoProibido() throws Exception {
        mockMvc.perform(delete("/produtos/123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="admin",roles = "ADMIN")
    void devePermitirAdminDeletarProduto() throws Exception {
        String id = "id";
//        when(produtoService.buscarProdutoPorId("123")).thenReturn(Optional.ofNullable(produtoModel));
        when(produtoService.buscarProdutoPorId(id)).thenReturn(Optional.ofNullable(produtoModel));
        mockMvc.perform(delete("/produtos/"+ id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
