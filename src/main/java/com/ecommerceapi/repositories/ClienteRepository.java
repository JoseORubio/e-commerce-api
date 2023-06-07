package com.ecommerceapi.repositories;

import com.ecommerceapi.models.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, UUID> {
    boolean existsByLogin(String login);
    boolean existsByLoginAndSenha(String login, String senha);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

    Optional<ClienteModel> findByLogin(String login);
    List<ClienteModel> findByOrderByNome();
    @Query(value = "select * from clientes where nome like %?1% order by nome",nativeQuery = true)
    Optional<List<ClienteModel>> pesquisarClientes(String nome);
}
