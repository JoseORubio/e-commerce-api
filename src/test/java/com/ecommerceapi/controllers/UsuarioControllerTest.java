package com.ecommerceapi.controllers;


import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.dtos.UsuarioViewDTO;
import com.ecommerceapi.mockedmodels.UsuarioDTOMock;
import com.ecommerceapi.mockedmodels.UsuarioModelMock;
import com.ecommerceapi.mockedmodels.UsuarioViewDTOMock;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.services.PapelDoUsuarioService;
import com.ecommerceapi.services.PapelService;
import com.ecommerceapi.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @InjectMocks
    UsuarioController usuarioController;
    @Mock
    UsuarioService usuarioService;
    @Mock
    PapelDoUsuarioService papelDoUsuarioService;
    @Mock
    PapelService papelService;

    @Autowired
    MockMvc mockMvc;

    UsuarioModel usuarioModel;
    String idUsuario, nomeUsuario;
    UsuarioDTO usuarioDTO;
    UsuarioViewDTO usuarioViewDTO;
    Page<UsuarioModel> listaUsuarios;
    IllegalArgumentException exception;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .alwaysDo(print())
                .build();

        usuarioModel = new UsuarioModelMock();
        idUsuario = usuarioModel.getId().toString();
        nomeUsuario = usuarioModel.getNome();
        usuarioDTO = new UsuarioDTOMock();
        usuarioViewDTO = new UsuarioViewDTOMock();
        listaUsuarios = new PageImpl<>(Collections.singletonList(usuarioModel));

        exception = new IllegalArgumentException("[\n" +
                "  {\n" +
                "    \"Campo\": \"nome\",\n" +
                "    \"Erros\": \"Não deve estar em branco. Deve conter apenas letras e obedeçer o padrão 'Ana da Silva Pereira' com ao menos a primeira letra maiúscula.\"\n" +
                "  }]");
        Mockito.lenient().when(usuarioService.pegarUsuarioLogado())
                .thenReturn(usuarioModel);
        Mockito.lenient().when(usuarioService.mostrarUsuarioLogado(usuarioModel))
                .thenReturn(usuarioViewDTO);
    }

    //CadastrarUsuarios
    @Test
    public void deveCadastrarUsuariosStatusCreated() throws Exception {
        when(usuarioService.validaCadastroUsuario(any(UsuarioDTO.class), any(BindingResult.class)))
                .thenReturn(usuarioModel);
       
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(usuarioDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void naoDeveCadastrarUsuariosStatusBadRequest() throws Exception {
        when(usuarioService.validaCadastroUsuario(any(UsuarioDTO.class), any(BindingResult.class)))
                .thenThrow(exception);
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(usuarioDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //AtualizarUsuarios
    @Test
    public void deveAtualizarUsuariosStatusOk() throws Exception {
        
        when(usuarioService.validaAtualizacaoUsuario(any(UsuarioModel.class), any(UsuarioDTO.class), any(BindingResult.class)))
                .thenReturn(usuarioModel);
       
        mockMvc.perform(put("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(usuarioDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void naoDeveAtualizarUsuariosDadosInvalidosStatusBadRequest() throws Exception {
        
        when(usuarioService.validaAtualizacaoUsuario(any(UsuarioModel.class), any(UsuarioDTO.class), any(BindingResult.class)))
                .thenThrow(exception);
        mockMvc.perform(put("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(usuarioDTO)))
                .andExpect(status().isBadRequest());
    }

    //DeletarUsuarioPorId
    @Test
    void deveDeletarUsuarioPorIdStatusOk() throws Exception {
        when(usuarioService.buscarUsuarioPorId(idUsuario))
                .thenReturn(Optional.of(usuarioModel));
        mockMvc.perform(delete("/usuarios/" + idUsuario))
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveDeletarUsuarioPorIdStatusBadRequest() throws Exception {
        when(usuarioService.buscarUsuarioPorId("123"))
                .thenThrow(IllegalArgumentException.class);
        mockMvc.perform(delete("/usuarios/" + "123"))
                .andExpect(content().string("Id inválida."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveDeletarUsuarioPorIdStatusNotFound() throws Exception {
        when(usuarioService.buscarUsuarioPorId(idUsuario))
                .thenReturn(Optional.empty());
        mockMvc.perform(delete("/usuarios/" + idUsuario))
                .andExpect(content().string("Usuário não encontrado."))
                .andExpect(status().isNotFound());
    }

    //DeletarUsuarioLogado
    @Test
    void deveDeletarUsuarioLogadoStatusOk() throws Exception {
        
        mockMvc.perform(delete("/usuarios"))
                .andExpect(status().isOk());
    }

    //BuscarUsuarioLogado
    @Test
    public void deveBuscarUsuariosLogadoStatusOk() throws Exception {
        
       
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk());
    }

    //BuscarUsuarios
    @Test
    public void deveBuscarUsuariosSemPageableStatusOk() throws Exception {

        when(usuarioService.buscarUsuarios(any(Pageable.class))).thenReturn(listaUsuarios);
        mockMvc.perform(get("/usuarios/todos"))
                .andExpect(status().isOk());
    }

    @Test
    public void deveBuscarUsuariosComPageableStatusOk() throws Exception {

        when(usuarioService.buscarUsuarios(any(Pageable.class))).thenReturn(listaUsuarios);
        mockMvc.perform(get("/usuarios/todos?page=1&size=10"))
                .andExpect(status().isOk());
    }

    //BuscarUsuariosPorId
    @Test
    void deveBuscarUsuarioPorIdStatusOk() throws Exception {
        when(usuarioService.buscarUsuarioPorId(idUsuario))
                .thenReturn(Optional.of(usuarioModel));
        mockMvc.perform(get("/usuarios/id/" + idUsuario))
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveBuscarUsuarioPorIdStatusBadRequest() throws Exception {
        when(usuarioService.buscarUsuarioPorId("123"))
                .thenThrow(IllegalArgumentException.class);
        mockMvc.perform(get("/usuarios/id/" + "123"))
                .andExpect(content().string("Id inválida."))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoDeveBuscarUsuarioPorIdStatusNotFound() throws Exception {
        when(usuarioService.buscarUsuarioPorId(idUsuario))
                .thenReturn(Optional.empty());
        mockMvc.perform(get("/usuarios/id/" + idUsuario))
                .andExpect(content().string("Usuário não encontrado."))
                .andExpect(status().isNotFound());
    }

    //BuscarUsuariosPorNome
    @Test
    void deveBuscarUsuarioPorNomeStatusOk() throws Exception {
        when(usuarioService.pesquisarUsuarios(anyString(), any(Pageable.class)))
                .thenReturn(Optional.of(listaUsuarios));
        mockMvc.perform(get("/usuarios/nome/" + nomeUsuario))
                .andExpect(status().isOk());
    }

    @Test
    void naoDeveBuscarUsuarioPorNomeStatusNotFound() throws Exception {
        Page<UsuarioModel> listaUsuariosVazia = new PageImpl<>(Collections.EMPTY_LIST);
        when(usuarioService.pesquisarUsuarios(anyString(), any(Pageable.class)))
                .thenReturn(Optional.ofNullable(listaUsuariosVazia));
        mockMvc.perform(get("/usuarios/nome/" + nomeUsuario))
                .andExpect(content().string("Nenhum usuário encontrado."))
                .andExpect(status().isNotFound());
    }


}
