package com.ecommerceapi.repositories;

import com.ecommerceapi.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
    boolean existsByLogin(String login);
    boolean existsByLoginAndSenha(String login, String senha);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

    Optional<UsuarioModel> findByLogin(String login);

    //Se der certo apagaro o findByLogin e existsByLoginAndSenha
     Optional<UsuarioModel> findByLoginAndSenha(String login, String senha);
    List<UsuarioModel> findByOrderByNome();
    @Query(value = "select * from clientes where nome like %?1% order by nome",nativeQuery = true)
    Optional<List<UsuarioModel>> pesquisarUsuarios(String nome);
}
