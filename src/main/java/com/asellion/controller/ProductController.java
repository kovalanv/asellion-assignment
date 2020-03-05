package com.asellion.controller;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asellion.model.Product;
import com.asellion.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public ResponseEntity<Object> getProductList(@RequestHeader(value = "username") String username,
			@RequestParam("name") Optional<String> name, @RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("sortBy") Optional<String> sortBy,
			@RequestParam("sortOrder") Optional<String> sortOrder) {

		return productService.retrieveProducts(name, pageSize, page, sortBy, sortOrder);

	}

	@PostMapping
	public ResponseEntity<Object> createProduct(@RequestHeader(value = "username") String username,
			@RequestBody Product product) throws JSONException, NumberFormatException, SignatureException,
			NoSuchAlgorithmException, InvalidKeySpecException {

		return productService.createProduct(product);

	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> updateProduct(@RequestHeader(value = "username") String username,
			@PathVariable long id, @RequestBody Product product) throws JSONException, NumberFormatException,
			SignatureException, NoSuchAlgorithmException, InvalidKeySpecException {

		return productService.updateProduct(id, product);

	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getProduct(@RequestHeader(value = "username") String username, @PathVariable long id)
			throws JSONException, NumberFormatException, SignatureException, NoSuchAlgorithmException,
			InvalidKeySpecException {

		return productService.getProduct(id);
	}

}
