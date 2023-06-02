package com.ecommerceapi.repositories;

import com.ecommerceapi.models.VendaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendaRepository extends JpaRepository<VendaModel, UUID> {

}
