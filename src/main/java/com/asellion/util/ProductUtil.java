package com.asellion.util;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.asellion.constant.ProductConstants;

public class ProductUtil {

	public static PageRequest getPageRequestObject(Optional<Integer> pageSize, Optional<Integer> page, String sortBy,
			String sortOrder) {

		int evalPageSize = pageSize.orElse(ProductConstants.INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? ProductConstants.INITIAL_PAGE : page.get() - 1;
		Sort sortObj = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);

		return PageRequest.of(evalPage, evalPageSize, sortObj);
	}

	public static PageRequest getPageRequestObject() {

		return getPageRequestObject(0, 1);
	}

	public static PageRequest getPageRequestObject(int pageSize, int page) {

		return PageRequest.of(page, pageSize);
	}

	public static ResponseEntity<Object> getResponseMap(String status, HttpStatus statusCode, String message,
			Object dataMap) {
		java.util.Map<String, Object> responseMap = new HashMap<>();
		responseMap.put(ProductConstants.STATUS, status);
		responseMap.put(ProductConstants.STATUS_CODE, statusCode.value());
		responseMap.put(ProductConstants.MESSAGE, message);
		if (dataMap != null) {
			responseMap.put(ProductConstants.RESPONSE_DATA, dataMap);
		}
		return new ResponseEntity<Object>(responseMap, statusCode);
	}

	public static ResponseEntity<Object> getSuccessResponseMap(String message) {
		return getResponseMap(ProductConstants.SUCCESS, HttpStatus.OK, message, null);
	}

	public static ResponseEntity<Object> getSuccessResponseMap(String message, Object dataMap) {
		return getResponseMap(ProductConstants.SUCCESS, HttpStatus.OK, message, dataMap);
	}

	public static ResponseEntity<Object> getFailureResponseMap(String message, Object dataMap) {

		return getResponseMap(ProductConstants.FAILURE, HttpStatus.OK, message, dataMap);
	}

	public static ResponseEntity<Object> getFailureResponseMap(HttpStatus statusCode, String message, Object dataMap) {

		return getResponseMap(ProductConstants.FAILURE, statusCode, message, dataMap);
	}

	public static ResponseEntity<Object> getFailureResponseMap(HttpStatus statusCode, String message) {

		return getResponseMap(ProductConstants.FAILURE, statusCode, message, null);
	}

}
