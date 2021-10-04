package com.fsm.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fsm.dscatalog.dto.ProductDTO;
import com.fsm.dscatalog.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private long existingId;
	private long nonExistId;
	private int totalProduct;
	

	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistId = 1000L;
		totalProduct = 25;		
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception{
		
		ResultActions result =
				mockMvc.perform(get("/products?page=0&size=5&sort=name")
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(totalProduct));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
	}
	
	@Test
	public void updateShouldReturnProductWhenIdExists() throws Exception {	
		
		ProductDTO product = Factory.createProductDTO();
		String productToString = objectMapper.writeValueAsString(product);
		String expectedName = product.getName();
		String expectedDescription = product.getDescription();
		ResultActions result =
				mockMvc.perform(put("/products/{id}",existingId)
						.content(productToString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescription));
		
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {		
		ProductDTO product = Factory.createProductDTO();
		String productToString = objectMapper.writeValueAsString(product);
		ResultActions result =
				mockMvc.perform(put("/products/{id}",nonExistId)
						.content(productToString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
		}
	

}
