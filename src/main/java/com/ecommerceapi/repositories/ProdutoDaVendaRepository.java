package com.ecommerceapi.repositories;

import com.ecommerceapi.models.ProdutoDaVendaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoDaVendaRepository extends JpaRepository<ProdutoDaVendaModel, UUID> {
}
