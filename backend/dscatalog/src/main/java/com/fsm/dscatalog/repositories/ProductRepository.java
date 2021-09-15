package com.fsm.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fsm.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
