package com.ecommerceapi.services;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.modelsbuilders.ProdutoStaticBuilder;
import com.ecommerceapi.models.ProdutoModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProdutoServiceIntegrationTest {

    @Autowired
    ProdutoService produtoService;

    @Autowired
    private Validator validator;

    ProdutoModel produtoModel;
    Page<ProdutoModel> listaProdutos;
    ProdutoDTO produtoDTO;
    BindException bindingResult;
    Set<ConstraintViolation<ProdutoDTO>> violations;
    Pageable pageable;
    IllegalArgumentException exception;

    @BeforeEach
    public void setup() {
        produtoModel = ProdutoStaticBuilder.getProdutoModelSemId();
        int page = 0;
        int size = 50;
        pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nome"));
    }

    //SalvarEApagar

    @Test
    void deveSalvarProdutoEDepoisApagar() {
        produtoService.salvarProduto(produtoModel);
        Optional<ProdutoModel> produtoModelOptional = produtoService.buscarProdutoPorId(produtoModel.getId().toString());
        assertTrue(produtoModelOptional.isPresent());
        produtoService.apagarProduto(produtoModelOptional.get());
        produtoModelOptional = produtoService.buscarProdutoPorId(produtoModel.getId().toString());
        assertTrue(produtoModelOptional.isEmpty());
    }

    //ValidarCadastro

    @Test
    void deveValidarCadastroProduto() {
        produtoDTO = ProdutoStaticBuilder.getProdutoDto();
        validarDtoEPreencherBindingResult();
        produtoModel = produtoService.validaCadastroProduto(produtoDTO, bindingResult);
        assertEquals(produtoModel.getNome(), produtoDTO.getNome());
    }

    //NaoValidarCadastro

    @Test
    void naoDeveValidarCadastroProdutoNomeInvalido() {
        produtoDTO = ProdutoStaticBuilder.getProdutoDtoNomeInvalido();
        validarDtoEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("nome"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    produtoService.validaCadastroProduto(produtoDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroProdutoPrecoInvalido() {
        produtoDTO = ProdutoStaticBuilder.getProdutoDtoPrecoInvalido();
        validarDtoEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("preco"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    produtoService.validaCadastroProduto(produtoDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroProdutoQuantidadeEstoqueInvalido() {
        produtoDTO = ProdutoStaticBuilder.getProdutoDtoQuantidadeEstoqueInvalida();
        validarDtoEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("quantidade_estoque"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    produtoService.validaCadastroProduto(produtoDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroProdutoAtributosNulos() {
        produtoDTO = ProdutoStaticBuilder.getProdutoDtoAtributosNulos();
        validarDtoEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("nome"));
        assertTrue(bindingResult.hasFieldErrors("preco"));
        assertTrue(bindingResult.hasFieldErrors("quantidade_estoque"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    produtoService.validaCadastroProduto(produtoDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroProdutoStringsVazias() {
        produtoDTO = ProdutoStaticBuilder.getProdutoDtoStringsVazias();
        validarDtoEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("nome"));
        assertTrue(bindingResult.hasFieldErrors("preco"));
        assertTrue(bindingResult.hasFieldErrors("quantidade_estoque"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    produtoService.validaCadastroProduto(produtoDTO, bindingResult);
                });
    }

    //BuscarProdutos

    @Test
    void deveBuscarProdutos() {
        listaProdutos = produtoService.buscarProdutos(pageable);
        for (ProdutoModel p : listaProdutos) {
            System.out.println(p.getNome());
        }
        assertTrue(listaProdutos.hasContent());
    }

    //BuscarProdutosPorId

    @Test
    void deveBuscarProdutosPorId() {
        Optional<ProdutoModel> produtoModelOptional = produtoService.buscarProdutoPorId("f9c473bf-afb8-4eb2-9d32-d1c2b9498408");
        assertTrue(produtoModelOptional.isPresent());
    }

    @Test
    void naoDeveBuscarProdutosPorIdInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    produtoService.buscarProdutoPorId("123");
                });
    }

    @Test
    void naoDeveBuscarProdutosPorIdVazia() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    produtoService.buscarProdutoPorId("");
                });
    }

    //PesquisarProdutos

    @Test
    void devePesquisarProdutosPorNome() {
        Optional<Page<ProdutoModel>> produtoModelOptionalPage = produtoService.pesquisarProdutos("Blusa Z", pageable);
        listaProdutos = produtoModelOptionalPage.get();
        for (ProdutoModel p : listaProdutos) {
            System.out.println(p.getNome());
            System.out.println(p.getId());
        }
        assertTrue(produtoModelOptionalPage.isPresent());
    }

    private void validarDtoEPreencherBindingResult() {
        bindingResult = new BindException(produtoDTO, "produto");
        violations = validator.validate(produtoDTO);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<ProdutoDTO> constraintViolation : violations) {
                String field = constraintViolation.getPropertyPath().toString();
                String message = constraintViolation.getMessage();
                FieldError fieldError = new FieldError("produto", field, message);
                bindingResult.addError(fieldError);
            }
        }
    }

    @Nested
    public class TestesQueNecessitamCriacaoERemocaoProduto {
        @BeforeEach
        void setupSalvaProduto() {
            produtoService.salvarProduto(produtoModel);
        }

        @AfterEach
        void setupApagaProduto() {
            produtoService.apagarProduto(produtoModel);
        }

        //NaoValidarCadastro

        @Test
        void naoDeveValidarCadastroProdutoNomeDuplicado() {
            produtoDTO = ProdutoStaticBuilder.getProdutoDto();
            validarDtoEPreencherBindingResult();
            exception = assertThrows(IllegalArgumentException.class,
                    () -> {
                        produtoService.validaCadastroProduto(produtoDTO, bindingResult);
                    });
            assertTrue(exception.getMessage().contains("Nome já utilizado."));
        }

        //ValidarAtualizacao

        @Test
        void deveValidarAtualizacaoProduto() {
            String nomeProdutoModificado = produtoModel.getNome() + "ABC";
            produtoDTO = ProdutoStaticBuilder.getProdutoDto();
            produtoDTO.setNome(nomeProdutoModificado);
            validarDtoEPreencherBindingResult();
            produtoModel = produtoService.validaAtualizacaoProduto(produtoModel, produtoDTO, bindingResult);
            assertEquals(produtoModel.getNome(), nomeProdutoModificado);

        }

        @Test
        void deveValidarAtualizacaoProdutoComMesmosDados() {

            produtoDTO = ProdutoStaticBuilder.getProdutoDto();
            validarDtoEPreencherBindingResult();
            ProdutoModel produtoModelDepoisDaValidacao = produtoService.validaAtualizacaoProduto(produtoModel, produtoDTO, bindingResult);
            assertEquals(produtoModelDepoisDaValidacao, produtoModel);

        }

        //NaoValidarAtualizacao

        @Test
        void naoDeveValidarAtualizacaoProdutoNomeInvalido() {

            produtoDTO = ProdutoStaticBuilder.getProdutoDtoNomeInvalido();
            validarDtoEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("nome"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        produtoService.validaAtualizacaoProduto(produtoModel, produtoDTO, bindingResult);
                    });

        }

        @Test
        void naoDeveValidarAtualizacaoProdutoPrecoInvalido() {

            produtoDTO = ProdutoStaticBuilder.getProdutoDtoPrecoInvalido();
            validarDtoEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("preco"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        produtoService.validaAtualizacaoProduto(produtoModel, produtoDTO, bindingResult);
                    });

        }

        @Test
        void naoDeveValidarAtualizacaoProdutoQuantidadeEstoqueInvalido() {

            produtoDTO = ProdutoStaticBuilder.getProdutoDtoQuantidadeEstoqueInvalida();
            validarDtoEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("quantidade_estoque"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        produtoService.validaAtualizacaoProduto(produtoModel, produtoDTO, bindingResult);
                    });

        }

        @Test
        void naoDeveValidarAtualizacaoProdutoAtributosNulos() {

            produtoDTO = ProdutoStaticBuilder.getProdutoDtoAtributosNulos();
            validarDtoEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("nome"));
            assertTrue(bindingResult.hasFieldErrors("preco"));
            assertTrue(bindingResult.hasFieldErrors("quantidade_estoque"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        produtoService.validaAtualizacaoProduto(produtoModel, produtoDTO, bindingResult);
                    });

        }

        @Test
        void naoDeveValidarAtualizacaoProdutoStringsVazias() {

            produtoDTO = ProdutoStaticBuilder.getProdutoDtoStringsVazias();
            validarDtoEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("nome"));
            assertTrue(bindingResult.hasFieldErrors("preco"));
            assertTrue(bindingResult.hasFieldErrors("quantidade_estoque"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        produtoService.validaAtualizacaoProduto(produtoModel, produtoDTO, bindingResult);
                    });

        }

        @Test
        void naoDeveValidarAtualizacaoProdutoNomeDuplicado() {

            String nomeProdutoExistente = produtoService.buscarProdutos(pageable).get().toList().get(0).getNome();
            produtoDTO = ProdutoStaticBuilder.getProdutoDto();
            produtoDTO.setNome(nomeProdutoExistente);
            validarDtoEPreencherBindingResult();
            exception = assertThrows(IllegalArgumentException.class,
                    () -> {
                        produtoService.validaAtualizacaoProduto(produtoModel, produtoDTO, bindingResult);
                    });
            assertTrue(exception.getMessage().contains("Nome já utilizado."));

        }

        //BaixarEstoqueProduto

        @Test
        void deveBaixarEstoqueProduto() {
            int quantidadeCompra = produtoModel.getQuantidade_estoque() - 1;
            produtoService.baixarEstoqueProduto(produtoModel, quantidadeCompra);
            assertEquals(produtoModel.getQuantidade_estoque(), 1);

        }


    }


}
