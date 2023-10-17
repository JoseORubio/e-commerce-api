package com.ecommerceapi.services;

import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.mockedmodels.ProdutoDTOMock;
import com.ecommerceapi.mockedmodels.ProdutoDTOStaticBuilder;
import com.ecommerceapi.mockedmodels.ProdutoModelMock;
import com.ecommerceapi.models.ProdutoModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
public class ProdutoServiceIntegrationTest {

    @Autowired
    ProdutoService produtoService;

    @Autowired
    private Validator validator;

    ProdutoModel produtoModel;
    //    String idProduto, nomeProduto;
    ProdutoDTO produtoDTO;
    BindException bindingResult;

    Set<ConstraintViolation<ProdutoDTO>> violations;

    Pageable pageable;
    Page<ProdutoModel> listaProdutos;
    IllegalArgumentException exception;

    @BeforeEach
    public void setup() {
        produtoModel = new ProdutoModel();
        produtoModel.setNome("Blusa Z");
        produtoModel.setQuantidade_estoque(10);
        produtoModel.setPreco(BigDecimal.valueOf(15.4));
//        idProduto = produtoModel.getId().toString();
//        nomeProduto = produtoModel.getNome();
//        produtoDTO = new ProdutoDTOMock();
        int page = 0;
        int size = 50;
        pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nome"));
//        listaProdutos = new PageImpl<>(Collections.singletonList(produtoModel));

        exception = new IllegalArgumentException("[\n" +
                "  {\n" +
                "    \"Campo\": \"preco\",\n" +
                "    \"Erros\": \"Não deve estar em branco. Deve ser qualquer valor monetário até 9999,99 com dois dígitos após , ou .\"\n" +
                "  }]");
    }


    @Test
    void deveSalvarProdutoEDepoisApagar() {
        produtoService.salvarProduto(produtoModel);
        System.out.println(produtoModel.getId().toString());
        System.out.println(produtoModel.getNome());

        Optional<ProdutoModel> produtoModelOptional = produtoService.buscarProdutoPorId(produtoModel.getId().toString());
        assertTrue(produtoModelOptional.isPresent());

        deveApagarProduto();

        produtoModelOptional = produtoService.buscarProdutoPorId(produtoModel.getId().toString());
        assertTrue(produtoModelOptional.isEmpty());
    }

    void deveApagarProduto() {
        Optional<ProdutoModel> produtoModelOptional = produtoService.buscarProdutoPorId(produtoModel.getId().toString());
        produtoService.apagarProduto(produtoModelOptional.get());
    }

    //ValidarCadastro
    @Test
    void deveValidarCadastroProduto() {
        produtoDTO = ProdutoDTOStaticBuilder.getProdutoDto();
        validarDtoEPreencherBindingResult();
        produtoModel = produtoService.validaCadastroProduto(produtoDTO, bindingResult);
        assertEquals(produtoModel.getNome(), produtoDTO.getNome());
    }

    @Test
    void naoDeveValidarCadastroProdutoIllegalArgumentException() {
        produtoDTO = ProdutoDTOStaticBuilder.getProdutoDtoPrecoInvalido();
        validarDtoEPreencherBindingResult();
        assertThrows(IllegalArgumentException.class,
                () -> {
                    produtoService.validaCadastroProduto(produtoDTO, bindingResult);
                });
    }

    private void validarDtoEPreencherBindingResult() {
        bindingResult = new BindException(produtoDTO, "prod");
        violations = validator.validate(produtoDTO);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<ProdutoDTO> constraintViolation : violations) {
                String field = constraintViolation.getPropertyPath().toString();
                String message = constraintViolation.getMessage();
                FieldError fieldError = new FieldError("prod", field, message);
                bindingResult.addError(fieldError);
            }
        }
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


}
