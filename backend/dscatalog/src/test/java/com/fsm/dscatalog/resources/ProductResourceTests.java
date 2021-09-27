package com.fsm.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fsm.dscatalog.dto.ProductDTO;
import com.fsm.dscatalog.services.ProductService;
import com.fsm.dscatalog.tests.Factory;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService service;

	ProductDTO product;

	PageImpl<ProductDTO> page;

	@BeforeEach
	void setup() throws Exception {
		product = Factory.createProductDTO();
		page = new PageImpl<>(List.of(product));

		when(service.findAll(any())).thenReturn(page);
	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result =
				mockMvc.perform(get("/products")
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}

}
