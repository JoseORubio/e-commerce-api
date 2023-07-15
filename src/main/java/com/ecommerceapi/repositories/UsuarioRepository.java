package com.ecommerceapi.repositories;

import com.ecommerceapi.models.UsuarioModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
    boolean existsByLogin(String login);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    Optional<UsuarioModel> findByLogin(String login);
    @Query(value = "select * from usuarios where nome like %?1% order by nome",nativeQuery = true)
    Optional<Page<UsuarioModel>> pesquisarUsuarios(String nome, Pageable pageable);
}
