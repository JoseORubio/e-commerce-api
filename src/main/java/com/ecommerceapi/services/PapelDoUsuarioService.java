package com.ecommerceapi.services;

import com.ecommerceapi.models.PapelDoUsuarioModel;
import com.ecommerceapi.repositories.PapelDoUsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PapelDoUsuarioService {
    @Autowired
    final PapelDoUsuarioRepository papelDoUsuarioRepository;

    public PapelDoUsuarioService(PapelDoUsuarioRepository papelDoUsuarioRepository) {
        this.papelDoUsuarioRepository = papelDoUsuarioRepository;
    }

    @Transactional
    public void salvarPapelDoUsuario(PapelDoUsuarioModel papelDoUsuarioModel){
        papelDoUsuarioRepository.save(papelDoUsuarioModel);
    }
}
