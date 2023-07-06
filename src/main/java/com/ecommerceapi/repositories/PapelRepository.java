package com.ecommerceapi.repositories;

import com.ecommerceapi.models.PapelModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PapelRepository extends JpaRepository<PapelModel, UUID> {
    Optional<PapelModel> findByNome(Enum nome);
}
