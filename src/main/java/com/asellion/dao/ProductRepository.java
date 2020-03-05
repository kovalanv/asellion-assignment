package com.asellion.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.asellion.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

	Page<Product> findByNameContains(String name, Pageable pageable);
	Product findFirstByNameIgnoreCase(String name);
}
