package com.ecommerceapi.services;

import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.dtos.UsuarioViewDTO;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.repositories.UsuarioRepository;
import com.ecommerceapi.utils.CEPUtils;
import com.ecommerceapi.utils.ConversorUUID;
import com.ecommerceapi.utils.ManipuladorListaErros;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

@Service
public class UsuarioService {
    @Autowired
    final UsuarioRepository usuarioRepository;


    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioModel salvarUsuario(UsuarioModel usuarioModel) {
        return usuarioRepository.save(usuarioModel);
    }


    public UsuarioModel validaCadastroUsuario(UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {

        ManipuladorListaErros manipuladorListaErros = new ManipuladorListaErros(errosDeValidacao);

        UsuarioModel usuarioModel = null;
        try {
            usuarioModel = validaPorRegrasDeNegocio(manipuladorListaErros, usuarioDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        usuarioModel.setNumeroRua(Integer.parseInt(usuarioDTO.getNumeroRua()));
        usuarioModel.setSexo(usuarioDTO.getSexo().charAt(0));
        usuarioModel.setDataCadastro(LocalDateTime.now(ZoneId.of("UTC")));
        usuarioModel.setSenha(new BCryptPasswordEncoder().encode(usuarioModel.getSenha()));

        return usuarioModel;
    }

    public UsuarioModel validaAtualizacaoUsuario(UsuarioModel usuarioLogado, UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {

        ManipuladorListaErros manipuladorListaErros = new ManipuladorListaErros(errosDeValidacao);

        if (usuarioDTO.getLogin() != null && usuarioDTO.getLogin().equals(usuarioLogado.getLogin()))
            usuarioDTO.setLogin(null);
        if (usuarioDTO.getCpf() != null && usuarioDTO.getCpf().equals(usuarioLogado.getCpf()))
            usuarioDTO.setCpf(null);
        if (usuarioDTO.getEmail() != null && usuarioDTO.getEmail().equals(usuarioLogado.getEmail()))
            usuarioDTO.setEmail(null);


        UsuarioModel usuarioAtualizado = null;
        try {
            usuarioAtualizado = validaPorRegrasDeNegocio(manipuladorListaErros, usuarioDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        }

        if (usuarioAtualizado.getLogin() == null) usuarioAtualizado.setLogin(usuarioLogado.getLogin());
        if (usuarioAtualizado.getCpf() == null) usuarioAtualizado.setCpf(usuarioLogado.getCpf());
        if (usuarioAtualizado.getEmail() == null) usuarioAtualizado.setEmail(usuarioLogado.getEmail());

        usuarioAtualizado.setId(usuarioLogado.getId());
        usuarioAtualizado.setDataCadastro(usuarioLogado.getDataCadastro());

        usuarioAtualizado.setNumeroRua(Integer.parseInt(usuarioDTO.getNumeroRua()));
        usuarioAtualizado.setSexo(usuarioDTO.getSexo().charAt(0));
        usuarioAtualizado.setSenha(new BCryptPasswordEncoder().encode(usuarioAtualizado.getSenha()));

        return usuarioAtualizado;
    }

    private UsuarioModel validaPorRegrasDeNegocio(ManipuladorListaErros manipuladorListaErros, UsuarioDTO usuarioDTO) {

        UsuarioModel usuarioModel = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDTO, usuarioModel);

        if (usuarioModel.getLogin() != null
                && existsByLogin(usuarioModel.getLogin())) {
            manipuladorListaErros.adicionarErros("login", "Login já utilizado.");
        }
        if (usuarioModel.getCpf() != null
                && existsByCpf(usuarioModel.getCpf())) {
            manipuladorListaErros.adicionarErros("cpf", "CPF já utilizado.");
        }
        if (usuarioModel.getEmail() != null
                && existsByEmail(usuarioModel.getEmail())) {
            manipuladorListaErros.adicionarErros("email", "Email já utilizado.");
        }

        if (usuarioModel.getCep() != null) {
            try {
                usuarioModel = new CEPUtils().retornaCep(usuarioModel);
            } catch (RuntimeException e) {
                manipuladorListaErros.adicionarErros("cep", "CEP não existe.");
            }
        }

        if (usuarioDTO.getDataNasc() != null) {
            try {
                usuarioModel.setDataNasc(LocalDate.parse(usuarioDTO.getDataNasc()
                        , DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)));
            } catch (DateTimeParseException e) {
                manipuladorListaErros.adicionarErros("dataNasc", "Data inválida.");
            }
        }

        if (manipuladorListaErros.temErros()) {
            String erros = manipuladorListaErros.converteListaErrosParaStringJson();
            throw new IllegalArgumentException(erros);
        }

        return usuarioModel;
    }

    @Transactional
    public void apagarUsuario(UsuarioModel usuarioModel) {
        usuarioRepository.delete(usuarioModel);
    }

    public UsuarioViewDTO mostrarUsuarioLogado(UsuarioModel usuarioModel) {
        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO();
        BeanUtils.copyProperties(usuarioModel, usuarioViewDTO);
        return usuarioViewDTO;
    }

    public Page<UsuarioModel> buscarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Optional<UsuarioModel> buscarUsuarioPorId(String idUsuario) {
        UUID id = ConversorUUID.converteUUID(idUsuario);
        if (idUsuario.equals("") || id == null)
            throw new IllegalArgumentException();
        return usuarioRepository.findById(id);
    }

    public Optional<Page<UsuarioModel>> pesquisarUsuarios(String nome, Pageable pageable) {
        return usuarioRepository.pesquisarUsuarios(nome, pageable);
    }

    public UsuarioModel pegarUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        String login = authentication.getName();
        UsuarioModel usuarioModel = usuarioRepository.findByLogin(login).get();
        return usuarioModel;
    }

    private boolean existsByLogin(String login) {
        return usuarioRepository.existsByLogin(login);
    }

    private boolean existsByCpf(String cpf) {
        return usuarioRepository.existsByCpf(cpf);
    }

    private boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }


}
