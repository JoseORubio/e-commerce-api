package com.ecommerceapi.services;

import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.dtos.UsuarioViewDTO;
import com.ecommerceapi.mockedmodels.builders.UsuarioStaticBuilder;
import com.ecommerceapi.models.UsuarioModel;
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

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UsuarioServiceIntegrationTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private Validator validator;

    UsuarioModel usuarioModel;
    Page<UsuarioModel> listaUsuarios;
    UsuarioDTO usuarioDTO;
    BindException bindingResult;
    Set<ConstraintViolation<UsuarioDTO>> violations;
    Pageable pageable;
    IllegalArgumentException exception;

    @BeforeEach
    public void setup() {
        usuarioModel = UsuarioStaticBuilder.getUsuarioModelSemId();
        int page = 0;
        int size = 50;
        pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nome"));
    }

//    @Test
    void apagarUsuarioThiago() {
        Optional<Page<UsuarioModel>> usuarioModelPage = usuarioService.pesquisarUsuarios(usuarioModel.getNome(), pageable);
        usuarioService.apagarUsuario(usuarioModelPage.get().getContent().get(0));
    }

    //SalvarEApagar
    @Test
    void deveSalvarUsuarioEDepoisApagar() {
        usuarioService.salvarUsuario(usuarioModel);
        Optional<UsuarioModel> usuarioModelOptional = usuarioService.buscarUsuarioPorId(usuarioModel.getId().toString());
        assertTrue(usuarioModelOptional.isPresent());
        usuarioService.apagarUsuario(usuarioModelOptional.get());
        usuarioModelOptional = usuarioService.buscarUsuarioPorId(usuarioModel.getId().toString());
        assertTrue(usuarioModelOptional.isEmpty());
    }

    //ValidarCadastro
    @Test
    void deveValidarCadastroUsuario() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTO();
        validarDTOEPreencherBindingResult();
        usuarioModel = usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
        assertEquals(usuarioModel.getNome(), usuarioDTO.getNome());
    }

    //NaoValidarCadastro
    @Test
    void naoDeveValidarCadastroUsuarioNomeInvalido() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTONomeInvalido();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("nome"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioLoginInvalido() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOLoginInvalido();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("login"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioSenhaInvalida() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOSenhaInvalida();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("senha"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioCPFInvalida() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOCPFInvalido();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("cpf"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioDataNascInvalida() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTODataNascInvalida();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("data_nasc"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioSexoInvalida() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOSexoInvalido();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("sexo"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioTelefoneInvalio() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOTelefoneInvalido();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("telefone"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioEmailInvalido() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOEmailInvalido();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("email"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioCEPInvalida() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOCEPInvalido();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("cep"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioNumeroRuaInvalida() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTONumeroRuaInvalido();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("numero_rua"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioAtributosNulos() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOAtributosNulos();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("nome"));
        assertTrue(bindingResult.hasFieldErrors("login"));
        assertTrue(bindingResult.hasFieldErrors("senha"));
        assertTrue(bindingResult.hasFieldErrors("cpf"));
        assertTrue(bindingResult.hasFieldErrors("data_nasc"));
        assertTrue(bindingResult.hasFieldErrors("sexo"));
        assertTrue(bindingResult.hasFieldErrors("telefone"));
        assertTrue(bindingResult.hasFieldErrors("email"));
        assertTrue(bindingResult.hasFieldErrors("cep"));
        assertTrue(bindingResult.hasFieldErrors("numero_rua"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }

    @Test
    void naoDeveValidarCadastroUsuarioStringsVazias() {
        usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOStringsVazias();
        validarDTOEPreencherBindingResult();
        assertTrue(bindingResult.hasFieldErrors("nome"));
        assertTrue(bindingResult.hasFieldErrors("login"));
        assertTrue(bindingResult.hasFieldErrors("senha"));
        assertTrue(bindingResult.hasFieldErrors("cpf"));
        assertTrue(bindingResult.hasFieldErrors("data_nasc"));
        assertTrue(bindingResult.hasFieldErrors("sexo"));
        assertTrue(bindingResult.hasFieldErrors("telefone"));
        assertTrue(bindingResult.hasFieldErrors("email"));
        assertTrue(bindingResult.hasFieldErrors("cep"));
        assertTrue(bindingResult.hasFieldErrors("numero_rua"));
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                });
    }


    //BuscarUsuarios
    @Test
    void deveBuscarUsuarios() {
        listaUsuarios = usuarioService.buscarUsuarios(pageable);
        for (UsuarioModel u : listaUsuarios) {
            System.out.println(u.getNome());
        }
        assertTrue(listaUsuarios.hasContent());
    }

    @Test
    void deveMostrarUsuarioLogado() {
        UsuarioViewDTO usuarioViewDTO = usuarioService.mostrarUsuarioLogado(usuarioModel);
        assertTrue(Optional.of(usuarioViewDTO).isPresent());
    }

    //BuscarUsuariosPorId
    @Test
    void deveBuscarUsuariosPorId() {
        Optional<UsuarioModel> usuarioModelOptional = usuarioService.buscarUsuarioPorId("2eb8d460-2ac3-48f0-bb2e-72cbd20c206c");
        assertTrue(usuarioModelOptional.isPresent());
    }

    @Test
    void naoDeveBuscarUsuariosPorIdInvalida() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.buscarUsuarioPorId("123");
                });
    }

    @Test
    void naoDeveBuscarUsuariosPorIdVazia() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    usuarioService.buscarUsuarioPorId("");
                });
    }

    //PesquisarUsuarios
    @Test
    void devePesquisarUsuariosPorNome() {
        Optional<Page<UsuarioModel>> usuarioModelOptionalPage = usuarioService.pesquisarUsuarios("Ber", pageable);
        listaUsuarios = usuarioModelOptionalPage.get();
        for (UsuarioModel p : listaUsuarios) {
            System.out.println(p.getNome());
            System.out.println(p.getId());
        }
        assertTrue(usuarioModelOptionalPage.isPresent());
    }

    private void validarDTOEPreencherBindingResult() {
        bindingResult = new BindException(usuarioDTO, "usuario");
        violations = validator.validate(usuarioDTO);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<UsuarioDTO> constraintViolation : violations) {
                String field = constraintViolation.getPropertyPath().toString();
                String message = constraintViolation.getMessage();
                FieldError fieldError = new FieldError("usuario", field, message);
                bindingResult.addError(fieldError);
            }
        }
    }

    @Nested
    public class TestesQueNecessitamCriacaoERemocaoUsuario {
        @BeforeEach
        void setupSalvaUsuario() {
            usuarioService.salvarUsuario(usuarioModel);
        }

        @AfterEach
        void setupApagaUsuario() {
            usuarioService.apagarUsuario(usuarioModel);
        }

        //NaoValidarCadastro

        @Test
        void naoDeveValidarCadastroUsuarioLoginCPFEmailDuplicado() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTO();
            validarDTOEPreencherBindingResult();
            exception = assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaCadastroUsuario(usuarioDTO, bindingResult);
                    });
            assertTrue(exception.getMessage().contains("Login já utilizado."));
            assertTrue(exception.getMessage().contains("CPF já utilizado."));
            assertTrue(exception.getMessage().contains("Email já utilizado."));
        }

        //ValidarAtualizacao

        @Test
        void deveValidarAtualizacaoUsuario() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTO2();
            validarDTOEPreencherBindingResult();
            usuarioModel = usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
            assertEquals(usuarioModel.getNome(), usuarioDTO.getNome());
            assertEquals(usuarioModel.getLogin(), usuarioDTO.getLogin());
            assertEquals(usuarioModel.getCpf(), usuarioDTO.getCpf());
            assertEquals(usuarioModel.getData_nasc().format(DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)), usuarioDTO.getData_nasc());
            assertEquals(usuarioModel.getSexo(), usuarioDTO.getSexo().charAt(0));
            assertEquals(usuarioModel.getTelefone(), usuarioDTO.getTelefone());
            assertEquals(usuarioModel.getEmail(), usuarioDTO.getEmail());
            assertEquals(usuarioModel.getCep(), usuarioDTO.getCep());
            assertEquals(String.valueOf(usuarioModel.getNumero_rua()), usuarioDTO.getNumero_rua());

        }

        @Test
        void deveValidarAtualizacaoUsuarioComMesmosDados() {

            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTO();
            validarDTOEPreencherBindingResult();
            UsuarioModel usuarioModelDepoisDaValidacao = usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
            assertEquals(usuarioModelDepoisDaValidacao, usuarioModel);

        }

        //NaoValidarAtualizacao

        @Test
        void naoDeveValidarAtualizacaoUsuarioNomeInvalido() {

            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTONomeInvalido();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("nome"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });

        }

        void naoDeveValidarCadastroUsuarioNomeInvalido() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTONomeInvalido();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("nome"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioLoginInvalido() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOLoginInvalido();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("login"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioSenhaInvalida() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOSenhaInvalida();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("senha"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioCPFInvalida() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOCPFInvalido();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("cpf"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioDataNascInvalida() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTODataNascInvalida();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("data_nasc"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioSexoInvalida() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOSexoInvalido();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("sexo"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioTelefoneInvalio() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOTelefoneInvalido();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("telefone"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioEmailInvalido() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOEmailInvalido();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("email"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioCEPInvalida() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOCEPInvalido();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("cep"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioNumeroRuaInvalida() {
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTONumeroRuaInvalido();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("numero_rua"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioAtributosNulos() {

            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOAtributosNulos();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("nome"));
            assertTrue(bindingResult.hasFieldErrors("login"));
            assertTrue(bindingResult.hasFieldErrors("senha"));
            assertTrue(bindingResult.hasFieldErrors("cpf"));
            assertTrue(bindingResult.hasFieldErrors("data_nasc"));
            assertTrue(bindingResult.hasFieldErrors("sexo"));
            assertTrue(bindingResult.hasFieldErrors("telefone"));
            assertTrue(bindingResult.hasFieldErrors("email"));
            assertTrue(bindingResult.hasFieldErrors("cep"));
            assertTrue(bindingResult.hasFieldErrors("numero_rua"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });

        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioStringsVazias() {

            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTOStringsVazias();
            validarDTOEPreencherBindingResult();
            assertTrue(bindingResult.hasFieldErrors("nome"));
            assertTrue(bindingResult.hasFieldErrors("login"));
            assertTrue(bindingResult.hasFieldErrors("senha"));
            assertTrue(bindingResult.hasFieldErrors("cpf"));
            assertTrue(bindingResult.hasFieldErrors("data_nasc"));
            assertTrue(bindingResult.hasFieldErrors("sexo"));
            assertTrue(bindingResult.hasFieldErrors("telefone"));
            assertTrue(bindingResult.hasFieldErrors("email"));
            assertTrue(bindingResult.hasFieldErrors("cep"));
            assertTrue(bindingResult.hasFieldErrors("numero_rua"));
            assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });

        }

        @Test
        void naoDeveValidarAtualizacaoUsuarioLoginCPFEmailDuplicado() {

            UsuarioModel usuarioModelBD = usuarioService.buscarUsuarios(pageable).get().toList().get(0);
//            String nomeUsuarioExistente = usuarioService.buscarUsuarios(pageable).get().toList().get(0).getNome();
            usuarioDTO = UsuarioStaticBuilder.getUsuarioDTO();
            usuarioDTO.setLogin(usuarioModelBD.getLogin());
            usuarioDTO.setCpf(usuarioModelBD.getCpf());
            usuarioDTO.setEmail(usuarioModelBD.getEmail());
            validarDTOEPreencherBindingResult();
            exception = assertThrows(IllegalArgumentException.class,
                    () -> {
                        usuarioService.validaAtualizacaoUsuario(usuarioModel, usuarioDTO, bindingResult);
                    });
            assertTrue(exception.getMessage().contains("Login já utilizado."));
            assertTrue(exception.getMessage().contains("CPF já utilizado."));
            assertTrue(exception.getMessage().contains("Email já utilizado."));

        }

    }


}
