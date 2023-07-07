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
    private List<List<String>> listaErros;
//    private List<Map<String, String>> listaErros;


    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioModel salvarUsuario(UsuarioModel usuarioModel) {
        return usuarioRepository.save(usuarioModel);
    }

    @Transactional
    public void delete(UsuarioModel usuarioModel) {
        usuarioRepository.delete(usuarioModel);
    }

    public boolean existsByLogin(String login) {
        return usuarioRepository.existsByLogin(login);
    }

    public boolean existsByCpf(String cpf) {
        return usuarioRepository.existsByCpf(cpf);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public List<UsuarioModel> buscarUsuarios() {
        return usuarioRepository.findByOrderByNome();
    }

    public Optional<UsuarioModel> buscarUsuarioPorId(String id_usuario) {
        UUID id = ValidacaoUtils.converteUUID(id_usuario);
        if (id_usuario.equals("") || id == null)
            throw new RuntimeException();
        return usuarioRepository.findById(id);
    }

    public Optional<UsuarioModel> buscarUsuarioPorLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    public Optional<List<UsuarioModel>> pesquisarUsuariosPorNome(String nome) {
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

    public Object validaUsuario(UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {

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
        } catch (DateTimeParseException e) {
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

    public Object validaCadastroUsuario(UsuarioDTO usuarioDTO, BindingResult errosDeValidacao) {

        validaPorBindingResult(errosDeValidacao);

//        UsuarioModel usuarioModel = new UsuarioModel();
//        BeanUtils.copyProperties(usuarioDTO, usuarioModel);

        UsuarioModel usuarioModel = validaPorRegrasNegocio(usuarioDTO);

        if (!listaErros.isEmpty()) {
            return listaErros;
        }

        usuarioModel.setNumero_rua(Integer.parseInt(usuarioDTO.getNumero_rua()));
        usuarioModel.setSexo(usuarioDTO.getSexo().charAt(0));
        usuarioModel.setData_cadastro(LocalDateTime.now(ZoneId.of("UTC")));
        usuarioModel.setSenha(new BCryptPasswordEncoder().encode(usuarioModel.getSenha()));

        return usuarioModel;

    }

//    private List<Map<String, String>> organizaErros(List<Map<String, String>> listaErros) {
//        List<Map<String, String>> listaErrosOrganizada = new ArrayList<>();
//        for(Map<String, String> erro : listaErros){
//            for(Map.Entry<String, String> errosValores : erro.entrySet()){
//
//            }
//        }
//
//        Map<String, String> infoItens = new LinkedHashMap<>();
//    }
public   void adicionarErros(String campoErro, String msgErro) {
//        Map<String, String> mapErro = new HashMap<String, String>();
//    Map<String, String> mapErro =  new LinkedHashMap<>();
//    List<String> camposEMensagens = new ArrayList<>();
//    mapErro.put("campo", campoErro);
//    mapErro.put("mensagem", msgErro);
    boolean campoRepetido= false;
    for (List<String> erro : listaErros){
        if (erro.get(0).equals(campoErro)){
            campoRepetido = true;
            erro.add(msgErro);
        }
    }
    if (!campoRepetido){
        List<String> camposEMensagens = new ArrayList<>();
        camposEMensagens.add(campoErro);
        camposEMensagens.add(msgErro);
        listaErros.add(camposEMensagens);
    }

//    return mapErro;
}
    private void validaPorBindingResult(BindingResult errosDeValidacao) {
        listaErros = new ArrayList<>();
        if (errosDeValidacao.hasErrors()) {
            List<FieldError> listaFieldErros = errosDeValidacao.getFieldErrors();
            for (FieldError erro : listaFieldErros) {
                System.out.println("!!!!!!!!!!!!!!!!!" + erro.getField());
                adicionarErros(erro.getField(), erro.getDefaultMessage());
//                listaErros.add(adicionarErros(erro.getField(), erro.getDefaultMessage()));
            }
        }
    }

    private UsuarioModel validaPorRegrasNegocio(UsuarioDTO usuarioDTO) {

        UsuarioModel usuarioModel = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDTO, usuarioModel);

        if (usuarioModel.getLogin() != null
                && existsByLogin(usuarioModel.getLogin())) {
            adicionarErros("login", "Login já utilizado.");
        }
        if (usuarioModel.getCpf() != null
                && existsByCpf(usuarioModel.getCpf())) {
            adicionarErros("cpf", "CPF já utilizado.");
        }
        if (usuarioModel.getEmail() != null
                && existsByEmail(usuarioModel.getEmail())) {
            adicionarErros("email", "Email já utilizado.");
        }

        if (usuarioModel.getCep() != null) {
            try {
                usuarioModel = new CEPUtils().retornaCep(usuarioModel);
            } catch (RuntimeException e) {
                adicionarErros("cep", "CEP não existe.");
            }
        }

        if (usuarioDTO.getData_nasc() != null) {
            try {
                usuarioModel.setData_nasc(LocalDate.parse(usuarioDTO.getData_nasc()
                        , DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)));
            } catch (DateTimeParseException e) {
                adicionarErros("data_nasc", "Data inválida.");
            }
        }
        return usuarioModel;
    }

}
