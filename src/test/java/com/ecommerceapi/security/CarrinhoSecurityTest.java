package com.ecommerceapi.security;

import com.ecommerceapi.modelsbuilders.CarrinhoStaticBuilder;
import com.ecommerceapi.modelsbuilders.ProdutoStaticBuilder;
import com.ecommerceapi.modelsbuilders.UsuarioStaticBuilder;
import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.services.CarrinhoService;
import com.ecommerceapi.services.ProdutoService;
import com.ecommerceapi.services.UsuarioService;
import com.ecommerceapi.services.VendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CarrinhoSecurityTest {

    @MockBean
    VendaService vendaService;
    @MockBean
    ProdutoService produtoService;
    @MockBean
    UsuarioService usuarioService;
    @MockBean
    CarrinhoService carrinhoService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private CarrinhoModel carrinhoModel;
    private UsuarioModel usuarioModel;
    private ProdutoModel produtoModel;
    private String idProduto, quantidadeProduto;
    private List<CarrinhoModel> carrinhoExistente;
    private List<Object> listaCarrinhoView;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        carrinhoModel = CarrinhoStaticBuilder.getMockCarrinhoModel();
        usuarioModel = UsuarioStaticBuilder.getMockUsuarioModelComId();
        produtoModel = ProdutoStaticBuilder.getMockProdutoModelComId();

        idProduto = produtoModel.getId().toString();
        quantidadeProduto = String.valueOf(produtoModel.getQuantidade_estoque());

        carrinhoExistente = new ArrayList<>(Collections.singletonList(carrinhoModel));
        listaCarrinhoView = new ArrayList<>(Collections.singletonList(carrinhoExistente));

        when(usuarioService.pegarUsuarioLogado())
                .thenReturn(usuarioModel);
    }

    //InserirProduto
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveInserirProdutoStatusUnauthorized() throws Exception {
        mockMvc.perform(put("/itens-carrinho"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveInserirProdutoStatusCreated() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        when(carrinhoService.validaInsercaoProduto(usuarioModel, produtoModel, quantidadeProduto))
                .thenReturn(carrinhoModel);

        userDeveVerCarrinhoStatusOk();

        mockMvc.perform(put("/itens-carrinho")
                        .param("id_produto", idProduto)
                        .param("quantidade", quantidadeProduto)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminNaoDeveInserirProdutoStatusForbidden() throws Exception {
        mockMvc.perform(put("/itens-carrinho"))
                .andExpect(status().isForbidden());
    }

    //RemoverProduto
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveRemoverProdutoStatusUnauthorized() throws Exception {
        mockMvc.perform(delete("/itens-carrinho/" + idProduto))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveRemoverProdutoStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        when(carrinhoService.buscarProdutoDoUsuarioNoCarrinho(usuarioModel, produtoModel))
                .thenReturn(Optional.of(carrinhoModel));

        mockMvc.perform(delete("/itens-carrinho/" + idProduto))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminNaoDeveRemoverProdutoStatusForbidden() throws Exception {
        mockMvc.perform(delete("/itens-carrinho/" + idProduto))
                .andExpect(status().isForbidden());
    }

    //CancelarCarrinho
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveCancelarCarrinhoStatusUnauthorized() throws Exception {
        mockMvc.perform(delete("/itens-carrinho"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveCancelarCarrinhoStatusOk() throws Exception {
        when(carrinhoService.buscarCarrinhoDoUsuario(usuarioModel))
                .thenReturn(Optional.of(carrinhoExistente));

        mockMvc.perform(delete("/itens-carrinho"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminNaoDeveCancelarCarrinhoStatusForbidden() throws Exception {
        mockMvc.perform(delete("/itens-carrinho"))
                .andExpect(status().isForbidden());
    }

    //VerCarrinho
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveVerCarrinhoStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/itens-carrinho"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveVerCarrinhoStatusOk() throws Exception {
        when(carrinhoService.buscarCarrinhoDoUsuario(usuarioModel))
                .thenReturn(Optional.of(carrinhoExistente));
        when(carrinhoService.gerarVisualizacaoCarrinho(carrinhoExistente))
                .thenReturn(listaCarrinhoView);

        mockMvc.perform(get("/itens-carrinho"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminNaoDeveVerCarrinhoStatusForbidden() throws Exception {
        mockMvc.perform(get("/itens-carrinho"))
                .andExpect(status().isForbidden());
    }

    //ConfirmarVenda
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveConfirmarVendaStatusUnauthorized() throws Exception {
        mockMvc.perform(post("/itens-carrinho/venda"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveConfirmarVendaStatusCreated() throws Exception {
        when(carrinhoService.buscarCarrinhoDoUsuario(usuarioModel))
                .thenReturn(Optional.of(carrinhoExistente));

        mockMvc.perform(post("/itens-carrinho/venda"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminNaoDeveConfirmarVendaStatusForbidden() throws Exception {
        mockMvc.perform(post("/itens-carrinho/venda"))
                .andExpect(status().isForbidden());
    }


}
