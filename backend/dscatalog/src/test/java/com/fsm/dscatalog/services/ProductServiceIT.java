package com.fsm.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.fsm.dscatalog.dto.ProductDTO;
import com.fsm.dscatalog.repositories.ProductRepository;
import com.fsm.dscatalog.services.exception.NotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
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
	public void deleteShouldDeleteProductWhenIdExists() {
		service.delete(existingId);
		
		Assertions.assertEquals(totalProduct - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(NotFoundException.class, () -> {
			service.delete(nonExistId);
		} );
		
	}
	
	@Test
	public void findAllShouldReturnPageWhenPage0Size10() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0,result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		
	}
	
	@Test
	public void findAllShouldReturnEmptyPageWhenPageDoesNotExist() {
		PageRequest pageRequest = PageRequest.of(20, 10);
		Page<ProductDTO> result = service.findAll(pageRequest);
		
		Assertions.assertTrue(result.isEmpty());		
		
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() {
		PageRequest pageRequest = PageRequest.of(0, 10,Sort.by("name"));
		Page<ProductDTO> result = service.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro",result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer",result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa",result.getContent().get(2).getName());
		
	}
	
	

}
