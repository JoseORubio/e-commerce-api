package com.ecommerceapi.repositories;

import com.ecommerceapi.models.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, UUID> {
    boolean existsByLogin(String login);
    boolean existsByCpf(String cpf);
}
