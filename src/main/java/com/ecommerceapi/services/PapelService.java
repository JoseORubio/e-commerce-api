package com.ecommerceapi.services;

import com.ecommerceapi.enums.RoleName;
import com.ecommerceapi.models.PapelModel;
import com.ecommerceapi.repositories.PapelRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PapelService{
    final PapelRepository papelRepository;

    public PapelService(PapelRepository papelRepository) {
        this.papelRepository = papelRepository;
    }

    public UUID pegarIdPapelUsuario(){
        Optional<PapelModel> papel = papelRepository.findByNome(RoleName.ROLE_USER);
        return papel.get().getId();
        }

}
