package com.asellion;

import static com.asellion.security.SecurityConstants.HEADER_AUTH_STRING;
import static com.asellion.security.SecurityConstants.HEADER_USERNAME_STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.asellion.model.LoginUser;
import com.asellion.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductRestControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	private static HttpHeaders headers = null;

	// #1
	@Test
	public void getProducts_withoutAuthHeaders_thenStatus403() throws Exception {

		mvc.perform(get("/products").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());

	}

	// #2
	@Test
	public void createProduct_thenStatus201() throws Exception {

		Product product = new Product();
		product.setName("Prod1");
		product.setCurrentPrice(new BigDecimal("999.99"));

		String inputJson = mapToJson(product);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/products").headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.CREATED.value(), status);
		String content = mvcResult.getResponse().getContentAsString();
		Product savedProduct = getProductData(content);
		assertNotNull(savedProduct.getId());

	}

	// #3
	@Test
	public void createProduct_duplicateName_thenStatus400() throws Exception {

		Product product = new Product();
		product.setName("Prod2");
		product.setCurrentPrice(new BigDecimal("100"));

		String inputJson = mapToJson(product);

		mvc.perform(MockMvcRequestBuilders.post("/products").headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		mvc.perform(MockMvcRequestBuilders.post("/products").headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andExpect(status().isBadRequest());

	}

	// #4
	@Test
	public void createProduct_emptyName_thenStatus400() throws Exception {

		Product product = new Product();
		product.setName("");
		product.setCurrentPrice(new BigDecimal("100"));

		String inputJson = mapToJson(product);

		mvc.perform(MockMvcRequestBuilders.post("/products").headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andExpect(status().isBadRequest());

	}

	// #5
	@Test
	public void updateProduct_emptyPrice_thenStatus400() throws Exception {

		Product product = new Product();
		product.setName("Prod3");

		String inputJson = mapToJson(product);

		mvc.perform(MockMvcRequestBuilders.post("/products").headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andExpect(status().isBadRequest());

	}

	// #6
	@Test
	public void updateProduct_thenStatus200() throws Exception {

		Product product = new Product();
		product.setName("Prod4");
		product.setCurrentPrice(new BigDecimal("100"));

		String inputJson = mapToJson(product);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/products").headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.CREATED.value(), status);
		String content = mvcResult.getResponse().getContentAsString();
		Product savedProduct = getProductData(content);

		product.setCurrentPrice(new BigDecimal("500"));
		inputJson = mapToJson(product);

		mvc.perform(MockMvcRequestBuilders.put("/products/" + savedProduct.getId()).headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andExpect(status().isOk());

	}

	// #7
	@Test
	public void updateProduct_invalidProductId_thenStatus400() throws Exception {

		Product product = new Product();
		product.setName("Prod5");
		product.setCurrentPrice(new BigDecimal("100"));

		String inputJson = mapToJson(product);

		mvc.perform(MockMvcRequestBuilders.put("/products/111110000").headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andExpect(status().isBadRequest());

	}

	// #8
	@Test
	public void getProductList_thenStatus200() throws Exception {

		Product product = new Product();
		product.setName("Prod6");
		product.setCurrentPrice(new BigDecimal("100"));

		String inputJson = mapToJson(product);

		mvc.perform(MockMvcRequestBuilders.post("/products").headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		MvcResult mvcResult = mvc
				.perform(get("/products").headers(getAuthHeaders()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), status);

		String content = mvcResult.getResponse().getContentAsString();
		Product[] prodList = getProductListData(content);

		assertNotNull(prodList);

	}

	// #9
	@Test
	public void getProduct_thenStatus200() throws Exception {

		Product product = new Product();
		product.setName("Prod7");
		product.setCurrentPrice(new BigDecimal("100"));

		String inputJson = mapToJson(product);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/products").headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.CREATED.value(), status);
		String content = mvcResult.getResponse().getContentAsString();
		Product savedProduct = getProductData(content);

		mvcResult = mvc.perform(get("/products/" + savedProduct.getId()).headers(getAuthHeaders())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

		status = mvcResult.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), status);

		content = mvcResult.getResponse().getContentAsString();
		Product dbProduct = getProductData(content);

		assertEquals(dbProduct.getName(), product.getName());

	}

	// #10
	@Test
	public void getProduct_invalidProductId_thenStatus400() throws Exception {

		mvc.perform(get("/products/9999999").headers(getAuthHeaders()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();

	}

	public HttpHeaders getAuthHeaders() throws Exception {

		if (headers != null) {
			return headers;
		}

		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("user1");
		loginUser.setPassword("1234");

		String inputJson = mapToJson(loginUser);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/login").content(inputJson))
				.andExpect(status().isOk()).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String jwtToken = mvcResult.getResponse().getHeader(HEADER_AUTH_STRING);
		String username = mvcResult.getResponse().getHeader(HEADER_USERNAME_STRING);

		headers = new HttpHeaders();
		headers.add("authorization", jwtToken);
		headers.add("username", username);

		return headers;
	}

	private String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}

	private <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	private Product getProductData(String responseJson)
			throws JSONException, JsonParseException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject(responseJson);
		String data = jsonObject.getString("data");
		return mapFromJson(data, Product.class);
	}

	private Product[] getProductListData(String responseJson)
			throws JSONException, JsonParseException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject(responseJson);
		JSONObject dataObject = jsonObject.getJSONObject("data");
		if (dataObject == null || dataObject.isNull("items")) {
			return null;
		}
		return mapFromJson(dataObject.getString("items"), Product[].class);
	}

}
