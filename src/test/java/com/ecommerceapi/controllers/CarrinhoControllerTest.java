package com.ecommerceapi.controllers;

import com.ecommerceapi.modelsbuilders.CarrinhoStaticBuilder;
import com.ecommerceapi.modelsbuilders.ProdutoStaticBuilder;
import com.ecommerceapi.modelsbuilders.UsuarioStaticBuilder;
import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CarrinhoControllerTest {

    @InjectMocks
    CarrinhoController carrinhoController;

    @Mock
    VendaService vendaService;
    @Mock
    ProdutoService produtoService;
    @Mock
    UsuarioService usuarioService;
    @Mock
    CarrinhoService carrinhoService;

    @Autowired
    MockMvc mockMvc;

    private CarrinhoModel carrinhoModel;
    private UsuarioModel usuarioModel;
    private ProdutoModel produtoModel;
    private String idProduto, quantidadeProduto;
    private List<CarrinhoModel> carrinhoExistente;
    private List<Object> listaCarrinhoView;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(carrinhoController)
                .alwaysDo(print())
                .build();

        carrinhoModel = CarrinhoStaticBuilder.getMockCarrinhoModel();
        usuarioModel = UsuarioStaticBuilder.getMockUsuarioModelComId();
        produtoModel = ProdutoStaticBuilder.getMockProdutoModelComId();

        idProduto = produtoModel.getId().toString();
        quantidadeProduto = String.valueOf(produtoModel.getQuantidade_estoque());

        carrinhoExistente = new ArrayList<>(Collections.singletonList(carrinhoModel));
        listaCarrinhoView = new ArrayList<>(Collections.singletonList(carrinhoExistente));

        Mockito.lenient().when(usuarioService.pegarUsuarioLogado())
                .thenReturn(usuarioModel);

    }


    //InserirProduto
    @Test
    void deveInserirProdutoStatusCreated() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        when(carrinhoService.validaInsercaoProduto(usuarioModel, produtoModel, quantidadeProduto))
                .thenReturn(carrinhoModel);

        deveVerCarrinhoStatusOk();

        mockMvc.perform(put("/itens-carrinho")
                        .param("id_produto", idProduto)
                        .param("quantidade", quantidadeProduto)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void naoDeveInserirProdutoIdInvalidaStatusBadRequest() throws Exception {
        when(produtoService.buscarProdutoPorId("123"))
                .thenThrow(IllegalArgumentException.class);

        mockMvc.perform(put("/itens-carrinho")
                        .param("id_produto", "123")
                        .param("quantidade", quantidadeProduto)
                )
                .andDo(print())
                .andExpect(content().string("Id inválida."))
                .andExpect(status().isBadRequest());

    }

    @Test
    void naoDeveInserirProdutoStatusNotFound() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/itens-carrinho")
                        .param("id_produto", idProduto)
                        .param("quantidade", quantidadeProduto)
                )
                .andDo(print())
                .andExpect(content().string("Produto não encontrado."))
                .andExpect(status().isNotFound());

    }

    @Test
    void naoDeveInserirProdutoDadosInvalidosStatusBadRequest() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        when(carrinhoService.validaInsercaoProduto(usuarioModel, produtoModel, quantidadeProduto))
                .thenThrow(IllegalArgumentException.class);

        mockMvc.perform(put("/itens-carrinho")
                        .param("id_produto", idProduto)
                        .param("quantidade", quantidadeProduto)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    //RemoverProduto
    @Test
    void deveRemoverProdutoStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        when(carrinhoService.buscarProdutoDoUsuarioNoCarrinho(usuarioModel, produtoModel))
                .thenReturn(Optional.of(carrinhoModel));

        mockMvc.perform(delete("/itens-carrinho/" + idProduto))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveRemoverProdutoIdInvalidaStatusBadRequest() throws Exception {
        when(produtoService.buscarProdutoPorId("123"))
                .thenThrow(IllegalArgumentException.class);
        mockMvc.perform(delete("/itens-carrinho/" + "123")
                        .param("quantidade", quantidadeProduto)
                )
                .andDo(print())
                .andExpect(content().string("Id inválida."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveRemoverProdutoStatusNotFound() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.empty());
        mockMvc.perform(delete("/itens-carrinho/" + idProduto)
                        .param("quantidade", quantidadeProduto)
                )
                .andDo(print())
                .andExpect(content().string("Produto não encontrado."))
                .andExpect(status().isNotFound());
    }

    @Test
    void naoDeveRemoverProdutoStatusOk() throws Exception {
        when(produtoService.buscarProdutoPorId(idProduto))
                .thenReturn(Optional.of(produtoModel));
        when(carrinhoService.buscarProdutoDoUsuarioNoCarrinho(usuarioModel, produtoModel))
                .thenReturn(Optional.empty());
        mockMvc.perform(delete("/itens-carrinho/" + idProduto)
                        .param("quantidade", quantidadeProduto)
                )
                .andDo(print())
                .andExpect(content().string("Produto não encontrado neste carrinho."))
                .andExpect(status().isOk());
    }

    //CancelarCarrinho
    @Test
    void deveCancelarCarrinhoStatusOk() throws Exception {
        when(carrinhoService.buscarCarrinhoDoUsuario(usuarioModel))
                .thenReturn(Optional.of(carrinhoExistente));

        mockMvc.perform(delete("/itens-carrinho"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveCancelarCarrinhoStatusBadRequest() throws Exception {
        carrinhoExistente = new ArrayList<>(Collections.EMPTY_LIST);
        when(carrinhoService.buscarCarrinhoDoUsuario(usuarioModel))
                .thenReturn(Optional.of(carrinhoExistente));

        mockMvc.perform(delete("/itens-carrinho"))
                .andDo(print())
                .andExpect(content().string("Usuário " + usuarioModel.getNome() + " não possui carrinho ativo."))
                .andExpect(status().isBadRequest());
    }

    //VerCarrinho
    @Test
    void deveVerCarrinhoStatusOk() throws Exception {
        when(carrinhoService.buscarCarrinhoDoUsuario(usuarioModel))
                .thenReturn(Optional.of(carrinhoExistente));
        when(carrinhoService.gerarVisualizacaoCarrinho(carrinhoExistente))
                .thenReturn(listaCarrinhoView);

        mockMvc.perform(get("/itens-carrinho"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveVerCarrinhoStatusBadRequest() throws Exception {
        carrinhoExistente = new ArrayList<>(Collections.EMPTY_LIST);
        when(carrinhoService.buscarCarrinhoDoUsuario(usuarioModel))
                .thenReturn(Optional.of(carrinhoExistente));

        mockMvc.perform(get("/itens-carrinho"))
                .andDo(print())
                .andExpect(content().string("Usuário " + usuarioModel.getNome() + " não possui carrinho ativo."))
                .andExpect(status().isBadRequest());
    }

    //ConfirmarVenda
    @Test
    void deveConfirmarVendaStatusCreated() throws Exception {
        when(carrinhoService.buscarCarrinhoDoUsuario(usuarioModel))
                .thenReturn(Optional.of(carrinhoExistente));

        mockMvc.perform(post("/itens-carrinho/venda"))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void naoDeveConfirmarVendaStatusBadRequest() throws Exception {
        carrinhoExistente = new ArrayList<>(Collections.EMPTY_LIST);
        when(carrinhoService.buscarCarrinhoDoUsuario(usuarioModel))
                .thenReturn(Optional.of(carrinhoExistente));

        mockMvc.perform(post("/itens-carrinho/venda"))
                .andDo(print())
                .andExpect(content().string("Usuário " + usuarioModel.getNome() + " não possui carrinho ativo."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveConfirmarVendaStatusConflict() throws Exception {
        IllegalArgumentException exception = new IllegalArgumentException("Usuário " + usuarioModel.getNome() + " não possui carrinho ativo.");

        when(carrinhoService.buscarCarrinhoDoUsuario(usuarioModel))
                .thenReturn(Optional.of(carrinhoExistente));
        doThrow(exception).when(vendaService).efetivaVendaCompleta(usuarioModel, carrinhoExistente);


        mockMvc.perform(post("/itens-carrinho/venda"))
                .andDo(print())
                .andExpect(content().string("Usuário " + usuarioModel.getNome() + " não possui carrinho ativo."))
                .andExpect(status().isConflict());
    }


}
