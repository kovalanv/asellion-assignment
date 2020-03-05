package com.asellion.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.asellion.model.Product;

public interface ProductService {

	public ResponseEntity<Object> retrieveProducts(Optional<String> name, Optional<Integer> pageSize,
			Optional<Integer> page, Optional<String> sortBy, Optional<String> sortOrder);

	public ResponseEntity<Object> updateProduct(long productId, Product product);

	public ResponseEntity<Object> createProduct(Product newProduct);

	public ResponseEntity<Object> getProduct(long productId);
}
