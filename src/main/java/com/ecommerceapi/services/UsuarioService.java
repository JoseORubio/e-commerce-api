package com.ecommerceapi.services;

import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
    @Autowired
    final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioModel salvarUsuarios(UsuarioModel usuarioModel) {
        return usuarioRepository.save(usuarioModel);
    }

    public boolean existsByLogin(String login) {
        return usuarioRepository.existsByLogin(login);
    }
    public boolean autorizaUsuario(String login, String senha) {
        return usuarioRepository.existsByLoginAndSenha(login, senha);
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

    public Optional<UsuarioModel> buscarUsuarioPorId(UUID id){
        return usuarioRepository.findById(id);
    }
    public Optional<UsuarioModel> buscarUsuarioPorLogin(String login){
        return usuarioRepository.findByLogin(login);
    }
    public Optional<List<UsuarioModel>> pesquisarUsuarios(String nome){
        return usuarioRepository.pesquisarUsuarios(nome);
    }

    public Optional<UsuarioModel> buscarUsuarioPorLoginESenha(String login, String senha){
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }

    @Transactional
    public void delete(UsuarioModel usuarioModel){
        usuarioRepository.delete(usuarioModel);
    }

}
