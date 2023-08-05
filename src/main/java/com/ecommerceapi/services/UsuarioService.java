package com.ecommerceapi.services;

import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.dtos.UsuarioViewDTO;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.repositories.UsuarioRepository;
import com.ecommerceapi.utils.CEPUtils;
import com.ecommerceapi.utils.ConversorUUID;
import com.ecommerceapi.utils.ValidatorUtils;
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


    public Object validaCadastroUsuario(UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {

        ValidatorUtils validatorUtils = new ValidatorUtils(errosDeValidacao);
        List<List<String>> listaErros = validatorUtils.getListaErros();

        Object validacaoUsuario = validaPorRegrasDeNegocio(listaErros, usuarioDTO);

        if (validacaoUsuario instanceof List<?>) {
            return ValidatorUtils.converteListaErrosParaMap((List<List<String>>) validacaoUsuario);
        }
        UsuarioModel usuarioModel = (UsuarioModel) validacaoUsuario;
        usuarioModel.setNumero_rua(Integer.parseInt(usuarioDTO.getNumero_rua()));
        usuarioModel.setSexo(usuarioDTO.getSexo().charAt(0));
        usuarioModel.setData_cadastro(LocalDateTime.now(ZoneId.of("UTC")));
        usuarioModel.setSenha(new BCryptPasswordEncoder().encode(usuarioModel.getSenha()));

        return usuarioModel;
    }

    public Object validaAtualizacaoUsuario(UsuarioModel usuarioLogado, UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {

        ValidatorUtils validatorUtils = new ValidatorUtils(errosDeValidacao);
        List<List<String>> listaErros = validatorUtils.getListaErros();

        if (usuarioDTO.getLogin() != null && usuarioDTO.getLogin().equals(usuarioLogado.getLogin()))
            usuarioDTO.setLogin(null);
        if (usuarioDTO.getCpf() != null && usuarioDTO.getCpf().equals(usuarioLogado.getCpf())) usuarioDTO.setCpf(null);
        if (usuarioDTO.getEmail() != null && usuarioDTO.getEmail().equals(usuarioLogado.getEmail()))
            usuarioDTO.setEmail(null);

        Object validacaoUsuario = validaPorRegrasDeNegocio(listaErros, usuarioDTO);

        if (validacaoUsuario instanceof List<?>) {
            return ValidatorUtils.converteListaErrosParaMap((List<List<String>>) validacaoUsuario);
        }

        UsuarioModel usuarioModel = (UsuarioModel) validacaoUsuario;

        if (usuarioModel.getLogin() == null) usuarioModel.setLogin(usuarioLogado.getLogin());
        if (usuarioModel.getCpf() == null) usuarioModel.setCpf(usuarioLogado.getCpf());
        if (usuarioModel.getEmail() == null) usuarioModel.setEmail(usuarioLogado.getEmail());

        usuarioModel.setId(usuarioLogado.getId());
        usuarioModel.setData_cadastro(usuarioLogado.getData_cadastro());

        usuarioModel.setNumero_rua(Integer.parseInt(usuarioDTO.getNumero_rua()));
        usuarioModel.setSexo(usuarioDTO.getSexo().charAt(0));
        usuarioModel.setSenha(new BCryptPasswordEncoder().encode(usuarioModel.getSenha()));

        return usuarioModel;
    }

    private Object validaPorRegrasDeNegocio(List<List<String>> listaErros, UsuarioDTO usuarioDTO) {

        ValidatorUtils validatorUtils = new ValidatorUtils(listaErros);
        UsuarioModel usuarioModel = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDTO, usuarioModel);

        if (usuarioModel.getLogin() != null
                && existsByLogin(usuarioModel.getLogin())) {
            validatorUtils.adicionarErros("login", "Login já utilizado.");
        }
        if (usuarioModel.getCpf() != null
                && existsByCpf(usuarioModel.getCpf())) {
            validatorUtils.adicionarErros("cpf", "CPF já utilizado.");
        }
        if (usuarioModel.getEmail() != null
                && existsByEmail(usuarioModel.getEmail())) {
            validatorUtils.adicionarErros("email", "Email já utilizado.");
        }

        if (usuarioModel.getCep() != null) {
            try {
                usuarioModel = new CEPUtils().retornaCep(usuarioModel);
            } catch (RuntimeException e) {
                validatorUtils.adicionarErros("cep", "CEP não existe.");
            }
        }

        if (usuarioDTO.getData_nasc() != null) {
            try {
                usuarioModel.setData_nasc(LocalDate.parse(usuarioDTO.getData_nasc()
                        , DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)));
            } catch (DateTimeParseException e) {
                validatorUtils.adicionarErros("data_nasc", "Data inválida.");
            }
        }

        if (!validatorUtils.getListaErros().isEmpty())
            return validatorUtils.getListaErros();
        return usuarioModel;
    }

    @Transactional
    public void delete(UsuarioModel usuarioModel) {
        usuarioRepository.delete(usuarioModel);
    }

    public UsuarioViewDTO mostrarUsuarioLogado(UsuarioModel usuarioModel) {
        UsuarioViewDTO usuarioViewDTO = new UsuarioViewDTO();
        BeanUtils.copyProperties(usuarioModel, usuarioViewDTO);
        return usuarioViewDTO;
    }

    public Page<UsuarioModel> buscarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll( pageable);
    }

    public Optional<UsuarioModel> buscarUsuarioPorId(String id_usuario) {
        UUID id = ConversorUUID.converteUUID(id_usuario);
        if (id_usuario.equals("") || id == null)
            throw new RuntimeException();
        return usuarioRepository.findById(id);
    }

    public Optional<Page<UsuarioModel>> pesquisarUsuarios(String nome,Pageable pageable) {
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
