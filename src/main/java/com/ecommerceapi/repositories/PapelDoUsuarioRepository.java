package com.ecommerceapi.repositories;

import com.ecommerceapi.models.PapelDoUsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PapelDoUsuarioRepository extends JpaRepository<PapelDoUsuarioModel, UUID> {

}
