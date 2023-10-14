package com.ecommerceapi.security;


import com.ecommerceapi.dtos.ProdutoDTO;
import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.mockedmodels.*;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.services.PapelDoUsuarioService;
import com.ecommerceapi.services.PapelService;
import com.ecommerceapi.services.ProdutoDaVendaService;
import com.ecommerceapi.services.UsuarioService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UsuarioSecurityTest {

    @MockBean
    PapelService papelService;
    @MockBean
    PapelDoUsuarioService papelDoUsuarioService;
    @MockBean
    UsuarioService usuarioService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private UsuarioModelMock usuarioModel;
    private UsuarioDTOMock usuarioDTO;
    private UsuarioViewDTOMock usuarioViewDTOMock;
    private String idUsuario, nome;
    private Page<UsuarioModel> listaUsuarios;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        usuarioModel = new UsuarioModelMock();
        usuarioDTO = new UsuarioDTOMock();
        usuarioViewDTOMock = new UsuarioViewDTOMock();
        idUsuario = usuarioModel.getId().toString();
        nome = usuarioModel.getNome();
        listaUsuarios = new PageImpl<>(Collections.singletonList(usuarioModel));
    }

    // BuscarUsuarioLogado
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveBuscarUsuarioLogadoStatusNaoAutorizado() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveBuscarUsuarioLogadoStatusOk() throws Exception {
        when(usuarioService.mostrarUsuarioLogado(usuarioService.pegarUsuarioLogado()))
                .thenReturn(usuarioViewDTOMock);
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveBuscarUsuarioLogadoStatusOk() throws Exception {
        when(usuarioService.mostrarUsuarioLogado(usuarioService.pegarUsuarioLogado()))
                .thenReturn(usuarioViewDTOMock);
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk());
    }

    // BuscarUsuarios
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveBuscarUsuariosStatusNaoAutorizado() throws Exception {
        mockMvc.perform(get("/usuarios/todos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNaoDeveBuscarUsuariosStatusProibido() throws Exception {
        mockMvc.perform(get("/usuarios/todos"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveBuscarUsuariosStatusOk() throws Exception {
        when(usuarioService.buscarUsuarios(any(Pageable.class)))
                .thenReturn(listaUsuarios);
        mockMvc.perform(get("/usuarios/todos"))
                .andExpect(status().isOk());
    }

    // BuscarUsuarioPorID
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveBuscarUsuarioPorIDStatusNaoAutorizado() throws Exception {
        mockMvc.perform(get("/usuarios/id/" + idUsuario))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNaoDeveBuscarUsuarioPorIDStatusProibido() throws Exception {
        mockMvc.perform(get("/usuarios/id/" + idUsuario))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveBuscarUsuarioPorIDStatusOk() throws Exception {
        when(usuarioService.buscarUsuarioPorId(idUsuario))
                .thenReturn(Optional.of(usuarioModel));
        mockMvc.perform(get("/usuarios/id/" + idUsuario))
                .andExpect(status().isOk());
    }

    // BuscarUsuarioPorNome
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveBuscarUsuarioPornomeStatusNaoAutorizado() throws Exception {
        mockMvc.perform(get("/usuarios/nome/" + nome))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNaoDeveBuscarUsuarioPornomeStatusProibido() throws Exception {
        mockMvc.perform(get("/usuarios/nome/" + nome))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveBuscarUsuarioPornomeStatusOk() throws Exception {
        when(usuarioService.pesquisarUsuarios(anyString(), any(Pageable.class)))
                .thenReturn(Optional.of(listaUsuarios));
        mockMvc.perform(get("/usuarios/nome/" + nome))
                .andExpect(status().isOk());
    }

    // DeletarUsuarios
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveDeletarUsuariosStatusNaoAutorizado() throws Exception {
        mockMvc.perform(delete("/usuarios/" + idUsuario))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void usuarioNaoDeveDeletarUsuariosStatusProibido() throws Exception {
        mockMvc.perform(delete("/usuarios/" + idUsuario))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveDeletarUsuariosStatusOk() throws Exception {
        when(usuarioService.buscarUsuarioPorId(idUsuario))
                .thenReturn(Optional.ofNullable(usuarioModel));
        mockMvc.perform(delete("/usuarios/" + idUsuario))
//                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // DeletarUsuarioLogado
    @Test
    @WithAnonymousUser
    void anonimoNaoDeveDeletarUsuarioLogadoStatusNaoAutorizado() throws Exception {
        mockMvc.perform(delete("/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void usuarioDeveDeletarUsuarioLogadoStatusOk() throws Exception {
        when(usuarioService.pegarUsuarioLogado())
                .thenReturn(usuarioModel);
        mockMvc.perform(delete("/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveDeletarUsuarioLogadoStatusOk() throws Exception {
        when(usuarioService.pegarUsuarioLogado())
                .thenReturn(usuarioModel);
        mockMvc.perform(delete("/usuarios"))
                .andExpect(status().isOk());
    }


    // CadastrarUsuarios
    @Test
    @WithAnonymousUser
    void anonimoDeveCadastrarUsuarioStatusCreated() throws Exception {
        when(usuarioService.validaCadastroUsuario(any(UsuarioDTO.class), any(BindingResult.class)))
                .thenReturn(usuarioModel);
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(usuarioDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveCadastrarUsuarioStatusCreated() throws Exception {
        when(usuarioService.validaCadastroUsuario(any(UsuarioDTO.class), any(BindingResult.class)))
                .thenReturn(usuarioModel);
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(usuarioDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveCadastrarUsuarioStatusCreated() throws Exception {

        when(usuarioService.validaCadastroUsuario(any(UsuarioDTO.class), any(BindingResult.class)))
                .thenReturn(usuarioModel);
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(usuarioDTO)))
                .andExpect(status().isCreated());
    }

    // AtualizarUsuarios
    @Test
    @WithAnonymousUser
    void anonimoDeveAtualizarUsuarioStatusNaoAutorizado() throws Exception {

        mockMvc.perform(put("/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNaoDeveAtualizarUsuarioStatusOk() throws Exception {
        when(usuarioService.pegarUsuarioLogado())
                .thenReturn(usuarioModel);
        when(usuarioService.validaAtualizacaoUsuario(any(UsuarioModel.class), any(UsuarioDTO.class), any(BindingResult.class)))
                .thenReturn(usuarioModel);
        mockMvc.perform(put("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(usuarioDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveAtualizarUsuarioStatusOk() throws Exception {

        when(usuarioService.pegarUsuarioLogado())
                .thenReturn(usuarioModel);
        when(usuarioService.validaAtualizacaoUsuario(any(UsuarioModel.class), any(UsuarioDTO.class), any(BindingResult.class)))
                .thenReturn(usuarioModel);
        mockMvc.perform(put("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(usuarioDTO)))
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
