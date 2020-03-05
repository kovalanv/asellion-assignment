package com.asellion.service.impl;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.asellion.constant.ProductConstants;
import com.asellion.dao.ProductRepository;
import com.asellion.model.PaginatedResponse;
import com.asellion.model.Product;
import com.asellion.service.ProductService;
import com.asellion.util.ProductUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	public ResponseEntity<Object> retrieveProducts(Optional<String> name, Optional<Integer> pageSize,
			Optional<Integer> page, Optional<String> sortBy, Optional<String> sortOrder) {

		String sortByStr = sortBy.orElse("name");
		String sortOrderStr = sortOrder.orElse("asc");
		String nameStr = name.orElse(ProductConstants.EMPTY_STRING);

		Pageable pageable = ProductUtil.getPageRequestObject(pageSize, page, sortByStr, sortOrderStr);

		Page<Product> productList = productRepository.findByNameContains(nameStr, pageable);

		PaginatedResponse<Product> responseData = new PaginatedResponse<Product>(productList.getTotalPages(),
				productList.getTotalElements(), productList.getSize(), productList.isFirst(), productList.isLast(),
				productList.getContent());

		return ProductUtil.getSuccessResponseMap("Products retrieved successfully", responseData);

	}

	public ResponseEntity<Object> updateProduct(long productId, Product product) {

		product.setId(productId);

		ResponseEntity<Object> errorResponse = this.validateProductBeforeSaving(product, true);
		if (errorResponse != null) {
			return errorResponse;
		}
		Product dbProduct = saveProduct(product);
		return ProductUtil.getSuccessResponseMap("Product updated succesfully", dbProduct);
	}

	public ResponseEntity<Object> createProduct(Product product) {

		ResponseEntity<Object> errorResponse = this.validateProductBeforeSaving(product, false);
		if (errorResponse != null) {
			return errorResponse;
		}
		Product dbProduct = saveProduct(product);
		return ProductUtil.getResponseMap(ProductConstants.SUCCESS, HttpStatus.CREATED, "Product created succesfully",
				dbProduct);
	}

	private ResponseEntity<Object> validateProductBeforeSaving(Product product, boolean prodUpdate) {
		if (product.getName() == null || product.getName().isEmpty()) {
			return ProductUtil.getFailureResponseMap(HttpStatus.BAD_REQUEST, "Product name is empty");
		}

		if (product.getCurrentPrice() == null) {
			return ProductUtil.getFailureResponseMap(HttpStatus.BAD_REQUEST, "Product price is empty");
		}

		if (prodUpdate) {

			if (product.getId() == null) {
				return ProductUtil.getFailureResponseMap(HttpStatus.BAD_REQUEST, "Product id is empty");
			}

			Optional<Product> prod = productRepository.findById(product.getId());

			if (!prod.isPresent()) {
				return ProductUtil.getFailureResponseMap(HttpStatus.BAD_REQUEST, "Product id is invalid");
			}
			
			Product existingProd = productRepository.findFirstByNameIgnoreCase(product.getName());
			if (!existingProd.getId().equals(product.getId())) {
				return ProductUtil.getResponseMap(ProductConstants.FAILURE, HttpStatus.BAD_REQUEST,
						"Product name already exists", existingProd);
			}

		} else {

			Product existingProd = productRepository.findFirstByNameIgnoreCase(product.getName());
			if (existingProd != null) {
				return ProductUtil.getResponseMap(ProductConstants.FAILURE, HttpStatus.BAD_REQUEST,
						"Product already exists", existingProd);
			}

		}

		return null;
	}

	private Product saveProduct(Product newProduct) {

		newProduct.setLastUpdate(Calendar.getInstance());
		return productRepository.save(newProduct);
	}

	public ResponseEntity<Object> getProduct(long productId) {
		Optional<Product> prod = productRepository.findById(productId);

		if (prod.isPresent()) {
			Product dbProduct = prod.get();
			return ProductUtil.getSuccessResponseMap("Product retrieved succesfully", dbProduct);
		}

		return ProductUtil.getFailureResponseMap(HttpStatus.BAD_REQUEST, "Product id is invalid");
	}
}
