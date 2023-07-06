package com.ecommerceapi.services;

import com.ecommerceapi.dtos.UsuarioDTO;
import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.repositories.UsuarioRepository;
import com.ecommerceapi.utils.CEPUtils;
import com.ecommerceapi.utils.ValidacaoUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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

    @Transactional
    public void delete(UsuarioModel usuarioModel){
        usuarioRepository.delete(usuarioModel);
    }

    public boolean existsByLogin(String login) {
        return usuarioRepository.existsByLogin(login);
    }
    public boolean existsByCpf(String cpf){
        return usuarioRepository.existsByCpf(cpf);
    }
    public boolean existsByEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public List<UsuarioModel> buscarUsuarios(){
        return usuarioRepository.findByOrderByNome();
    }

    public Optional<UsuarioModel> buscarUsuarioPorId(String id_usuario){
        UUID id = ValidacaoUtils.converteUUID(id_usuario);
        if (id_usuario.equals("") || id == null)
            throw new RuntimeException();
        return usuarioRepository.findById(id);
    }
    public Optional<UsuarioModel> buscarUsuarioPorLogin(String login){
        return usuarioRepository.findByLogin(login);
    }
    public Optional<List<UsuarioModel>> pesquisarUsuariosPorNome(String nome){
        return usuarioRepository.pesquisarUsuarios(nome);
    }

    public UsuarioModel pegarUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof AnonymousAuthenticationToken)) {
            return null;
        }
        String login = authentication.getName();
        UsuarioModel usuarioModel = buscarUsuarioPorLogin(login).get();
        return usuarioModel;
    }

    public Object validaUsuario(UsuarioDTO usuarioDTO, BindingResult errosDeValidacao){
        List<Map<String, String>> listaErros = new ArrayList<>();
        if (errosDeValidacao.hasErrors()) {
            List<FieldError> listaFieldErros = errosDeValidacao.getFieldErrors();
            for (FieldError erro : listaFieldErros) {
                listaErros.add(ValidacaoUtils.adicionarErros(erro.getField(), erro.getDefaultMessage()));
            }
        }

        UsuarioModel usuarioModel = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDTO, usuarioModel);

        if (existsByLogin(usuarioModel.getLogin())) {
            listaErros.add(ValidacaoUtils.adicionarErros("login", "Login já utilizado."));
        }
        if (existsByCpf(usuarioModel.getCpf())) {
            listaErros.add(ValidacaoUtils.adicionarErros("cpf", "CPF já utilizado."));
        }
        if (existsByEmail(usuarioModel.getEmail())) {
            listaErros.add(ValidacaoUtils.adicionarErros("email", "Email já utilizado."));
        }

        try {
            usuarioModel = new CEPUtils().retornaCep(usuarioModel);
        } catch (RuntimeException e) {
            listaErros.add(ValidacaoUtils.adicionarErros("cep", "CEP não existe."));
        }

        try {
            usuarioModel.setData_nasc(LocalDate.parse(usuarioDTO.getData_nasc()
                    , DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)));
        } catch ( DateTimeParseException e) {
            listaErros.add(ValidacaoUtils.adicionarErros("data_nasc", "Data inválida."));
        }

        if (!listaErros.isEmpty()) {
            return listaErros;
        }

        usuarioModel.setNumero_rua(Integer.parseInt(usuarioDTO.getNumero_rua()));
        usuarioModel.setSexo(usuarioDTO.getSexo().charAt(0));
        usuarioModel.setData_cadastro(LocalDateTime.now(ZoneId.of("UTC")));
        usuarioModel.setSenha(new BCryptPasswordEncoder().encode(usuarioModel.getSenha()));

        return usuarioModel;

    }

}
