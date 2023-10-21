package com.ecommerceapi.services;

import com.ecommerceapi.dtos.CarrinhoViewDTO;
import com.ecommerceapi.mockedmodels.builders.ProdutoStaticBuilder;
import com.ecommerceapi.models.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static java.lang.Math.ceil;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarrinhoVendaServiceIntegrationTest {
    @Autowired
    CarrinhoService carrinhoService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ProdutoService produtoService;
    @Autowired
    VendaService vendaService;
    @Autowired
    ProdutoDaVendaService produtoDaVendaService;

    UsuarioModel usuarioModel;
    ProdutoModel produtoModel1, produtoModel2, produtoModel3;
    String metadeQuantidadeEstoqueProduto1String, metadeQuantidadeEstoqueProduto2String, metadeQuantidadeEstoqueProduto3String;
    CarrinhoModel carrinhoModel;
    IllegalArgumentException exception;

    @BeforeAll
    void beforeAll() {
        usuarioModel = usuarioService.buscarUsuarioPorId("2eb8d460-2ac3-48f0-bb2e-72cbd20c206c").get();
        produtoModel1 = ProdutoStaticBuilder.getProdutoModelSemId();
        produtoModel1 = produtoService.salvarProduto(produtoModel1);
        metadeQuantidadeEstoqueProduto1String = String.valueOf((int) ceil(produtoModel1.getQuantidade_estoque() / 2.0));
        produtoModel2 = ProdutoStaticBuilder.getProdutoModelSemId2();
        produtoModel2 = produtoService.salvarProduto(produtoModel2);
        metadeQuantidadeEstoqueProduto2String = String.valueOf((int) ceil(produtoModel2.getQuantidade_estoque() / 2.0));
        produtoModel3 = ProdutoStaticBuilder.getProdutoModelSemId3();
        produtoModel3 = produtoService.salvarProduto(produtoModel3);
        metadeQuantidadeEstoqueProduto3String = String.valueOf((int) ceil(produtoModel3.getQuantidade_estoque() / 2.0));
    }

    @AfterAll
    void afterAll() {
        produtoService.apagarProduto(produtoModel1);
        produtoService.apagarProduto(produtoModel2);
        produtoService.apagarProduto(produtoModel3);
    }

    //    @Test
//    void consertaFalhaRemocaoCarrinho() {
//        Optional<List<CarrinhoModel>> carrinhoUsuario = carrinhoService.buscarCarrinhoDoUsuario(usuarioModel);
//        if (!carrinhoUsuario.get().isEmpty()) {
//            for (CarrinhoModel carrinho : carrinhoUsuario.get()) {
//                carrinhoService.apagarItemCarrinho(carrinho);
//            }
//        }
//        ProdutoModel produtoModel = new ProdutoModel();
//        Optional<Page<ProdutoModel>> produtoModelOptionalPage = produtoService.pesquisarProdutos("Blusa Z", pageable);
//        produtoModel = produtoModelOptionalPage.get().getContent().get(0);
//        produtoService.apagarProduto(produtoModel);
//        produtoModelOptionalPage = produtoService.pesquisarProdutos("Tênis W", pageable);
//        produtoModel = produtoModelOptionalPage.get().getContent().get(0);
//        produtoService.apagarProduto(produtoModel);
//        produtoModelOptionalPage = produtoService.pesquisarProdutos("Meia Y", pageable);
//        produtoModel = produtoModelOptionalPage.get().getContent().get(0);
//        produtoService.apagarProduto(produtoModel);
//    }

    //SalvarCarrinho
    @Test
    void deveSalvarCarrinhoDepoisApagar() {
        carrinhoModel = carrinhoService.validaInsercaoProduto
                (usuarioModel, produtoModel1, metadeQuantidadeEstoqueProduto1String);
        assertEquals(carrinhoModel.getUsuario(), usuarioModel);
        assertEquals(carrinhoModel.getProduto(), produtoModel1);

        carrinhoModel = carrinhoService.inserirCarrinho(carrinhoModel);
        assertTrue(carrinhoModel.getId() != null);
        apagarCarrinhoBernardo();
    }

    @Test
    void deveSalvarCarrinhoCom3ItensDepoisApagar() {
        carrinhoModel = carrinhoService.validaInsercaoProduto
                (usuarioModel, produtoModel1, "1");
        carrinhoService.inserirCarrinho(carrinhoModel);

        carrinhoModel = carrinhoService.validaInsercaoProduto
                (usuarioModel, produtoModel2, "1");
        carrinhoService.inserirCarrinho(carrinhoModel);

        carrinhoModel = carrinhoService.validaInsercaoProduto
                (usuarioModel, produtoModel3, "1");
        carrinhoService.inserirCarrinho(carrinhoModel);

        List<CarrinhoModel> listaCarrinho = carrinhoService.buscarCarrinhoDoUsuario(usuarioModel).get();
        assertTrue(listaCarrinho.size() == 3);
        apagarCarrinhoBernardo();
    }

    @Test
    void deveAtualizarQuantidadeProdutoCarrinho() {
        carrinhoModel = carrinhoService.validaInsercaoProduto
                (usuarioModel, produtoModel1, metadeQuantidadeEstoqueProduto1String);
        carrinhoService.inserirCarrinho(carrinhoModel);

        carrinhoModel = carrinhoService.validaInsercaoProduto
                (usuarioModel, produtoModel1, "1");
        carrinhoService.inserirCarrinho(carrinhoModel);

        assertTrue(carrinhoModel.getQuantidade() == 1);
        apagarCarrinhoBernardo();
    }

    //ValidarInsercaoProduto
    @Test
    void deveValidarInsercaoProduto() {
        carrinhoModel = carrinhoService.validaInsercaoProduto
                (usuarioModel, produtoModel1, metadeQuantidadeEstoqueProduto1String);
        assertEquals(carrinhoModel.getUsuario(), usuarioModel);
        assertEquals(carrinhoModel.getProduto(), produtoModel1);
    }

    //NaoValidarInsercaoProduto
    @Test
    void naoDeveValidarInsercaoProdutoQuantidadeNula() {
        exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    carrinhoService.validaInsercaoProduto
                            (usuarioModel, produtoModel1, null);
                });
        assertTrue(exception.getMessage().contains("Quantidade inválida."));
    }

    @Test
    void naoDeveValidarInsercaoProdutoQuantidadeMenorUm() {
        exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    carrinhoService.validaInsercaoProduto
                            (usuarioModel, produtoModel1, "0");
                });
        assertTrue(exception.getMessage().contains("Quantidade inválida."));
    }

    @Test
    void naoDeveValidarInsercaoProdutoQuantidadeMaiorQueEstoque() {
        exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    carrinhoService.validaInsercaoProduto
                            (usuarioModel, produtoModel1, String.valueOf(produtoModel1.getQuantidade_estoque() + 1));
                });
        assertTrue(exception.getMessage().contains("Quantidade indisponível no estoque."));
    }

    @Nested
    public class TestesQueNecessitamCarrinhoCompleto {
        @BeforeEach
        void criaCarrinho3Produtos() {
            carrinhoModel = carrinhoService.validaInsercaoProduto
                    (usuarioModel, produtoModel1, metadeQuantidadeEstoqueProduto1String);
            carrinhoService.inserirCarrinho(carrinhoModel);

            carrinhoModel = carrinhoService.validaInsercaoProduto
                    (usuarioModel, produtoModel2, metadeQuantidadeEstoqueProduto2String);
            carrinhoService.inserirCarrinho(carrinhoModel);

            carrinhoModel = carrinhoService.validaInsercaoProduto
                    (usuarioModel, produtoModel3, metadeQuantidadeEstoqueProduto3String);
            carrinhoService.inserirCarrinho(carrinhoModel);
        }

        @AfterEach
        void ApagarCarrinho() {
            apagarCarrinhoBernardo();
        }

        //BuscarCarrinho
        @Test
        void deveBuscarCarrinhoDoUsuario() {
            Optional<List<CarrinhoModel>> carrinho = carrinhoService.buscarCarrinhoDoUsuario(usuarioModel);
            assertTrue(carrinho.get().size() == 3);
        }

        @Test
        void deveBuscarProdutoNoCarrinhoDoUsuario() {
            Optional<CarrinhoModel> carrinho = carrinhoService.buscarProdutoDoUsuarioNoCarrinho(usuarioModel, produtoModel2);
            assertTrue(carrinho.isPresent());
        }

        //ApagarCarrinho
        @Test
        void deveApagarProdutoNoCarrinhoDoUsuario() {
            CarrinhoModel itemCarrinho = carrinhoService.buscarProdutoDoUsuarioNoCarrinho(usuarioModel, produtoModel2).get();
            carrinhoService.apagarItemCarrinho(itemCarrinho);
            Optional<List<CarrinhoModel>> carrinho = carrinhoService.buscarCarrinhoDoUsuario(usuarioModel);
            assertTrue(carrinho.get().size() == 2);
        }

        //GerarVisualizacaoCarrinho
        @Test
        void deveGerarVisualizacaoCarrinho() {
            Optional<List<CarrinhoModel>> carrinho = carrinhoService.buscarCarrinhoDoUsuario(usuarioModel);
            List<Object> listaCarrinhoView = carrinhoService.gerarVisualizacaoCarrinho(carrinho.get());

            for (int i = 0; i < listaCarrinhoView.size(); i++) {
                if (i < (listaCarrinhoView.size() - 1)) {
                    assertTrue(listaCarrinhoView.get(i) instanceof CarrinhoViewDTO);
                } else

                    assertTrue(listaCarrinhoView.get(i) instanceof Map<?, ?>);

            }
        }

        @Nested
        public class TestesEfetivacaoVenda {

            List<Object> vendaComProdutos;
            @BeforeEach
            void setup() {
                vendaComProdutos = new ArrayList<>();
            }

            @AfterEach
            void apagaVenda() {
                apagarProdutosVendidosEVenda();
                apagarCarrinhoBernardo();
            }

            //DeveEfetivarVenda
            @Test
            void deveEfetivarVendaCompleta() {
                List<CarrinhoModel> carrinhoModelList = carrinhoService.buscarCarrinhoDoUsuario(usuarioModel).get();
                vendaComProdutos = vendaService.efetivaVendaCompleta(usuarioModel, carrinhoModelList);
                assertFalse(vendaComProdutos.isEmpty());
            }

            //NaoDeveEfetivarVenda
            @Test
            void naoDeveEfetivarVendaCompletaQuantidadeCarrinhoMaiorEstoque() {
                List<CarrinhoModel> carrinhoModelList = carrinhoService.buscarCarrinhoDoUsuario(usuarioModel).get();
                carrinhoModelList.get(0).setQuantidade(carrinhoModelList.get(0).getProduto().getQuantidade_estoque() +1);

                exception = assertThrows(IllegalArgumentException.class,
                        () -> {
                            vendaService.efetivaVendaCompleta(usuarioModel, carrinhoModelList);
                        });
                assertTrue(exception.getMessage().contains("indisponível no estoque."));
            }

            private void apagarProdutosVendidosEVenda() {
                for (int i = vendaComProdutos.size() - 1; i >= 0; i--) {
                    if (i > 0) {
                        produtoDaVendaService.apagarProdutoVendido((ProdutoDaVendaModel) vendaComProdutos.get(i));
                    } else vendaService.apagarVenda((VendaModel) vendaComProdutos.get(i));
                }
            }
        }
    }

    private void apagarCarrinhoBernardo() {
        Optional<List<CarrinhoModel>> carrinhoUsuario = carrinhoService.buscarCarrinhoDoUsuario(usuarioModel);
        if (!carrinhoUsuario.get().isEmpty()) {
            for (CarrinhoModel carrinho : carrinhoUsuario.get()) {
                carrinhoService.apagarItemCarrinho(carrinho);
            }
        }
        carrinhoUsuario = carrinhoService.buscarCarrinhoDoUsuario(usuarioModel);
        assertTrue(carrinhoUsuario.get().isEmpty());
    }
}
