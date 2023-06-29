package com.ecommerceapi.security;

import com.ecommerceapi.models.UsuarioModel;
import com.ecommerceapi.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioModel usuarioModel = usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Não encontrado nenhum usuario com o login: " + username));
        return new User(usuarioModel.getLogin()
                , usuarioModel.getPassword()
                ,true,true,true,true
                ,usuarioModel.getAuthorities());
    }
}
