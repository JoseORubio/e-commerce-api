package com.ecommerceapi.repositories;

import com.ecommerceapi.models.ProdutoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, UUID> {
    boolean existsByNome(String nome);
    @Query(value = "select * from produtos where nome like %?1% ",nativeQuery = true)
    Optional<Page<ProdutoModel>> pesquisarProdutos( String nome ,Pageable pageable );

}
