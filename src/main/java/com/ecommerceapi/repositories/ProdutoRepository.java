package com.ecommerceapi.repositories;

import com.ecommerceapi.models.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, UUID> {
    boolean existsByNome(String nome);

    List<ProdutoModel> findByOrderByNome();
    @Query(value = "select * from produtos where nome like %?1% order by nome",nativeQuery = true)
    Optional<List<ProdutoModel>> pesquisarProdutos(String nome);

//    @Query(value = "UPDATE `e-commerce`.`produtos` SET `quantidade_estoque` = '10002' WHERE (`id` = 0x5F47A7E0A2C34F02B5E69C75AEEFB2A4)", nativeQuery = true)
//    void baixaEstoque(UUID id, int quantidade);
}
