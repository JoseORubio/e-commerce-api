package com.ecommerceapi.repositories;

import com.ecommerceapi.models.CarrinhoModel;
import com.ecommerceapi.models.ProdutoDaVendaModel;
import com.ecommerceapi.models.ProdutoModel;
import com.ecommerceapi.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarrinhoRepository extends JpaRepository<CarrinhoModel, UUID> {
    Optional<CarrinhoModel> findByUsuarioAndProduto(UsuarioModel usuario, ProdutoModel produto);
    Optional<List<CarrinhoModel>> findByUsuario(UsuarioModel usuario);

}
