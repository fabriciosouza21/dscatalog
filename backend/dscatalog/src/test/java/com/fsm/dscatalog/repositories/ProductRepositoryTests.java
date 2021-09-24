package com.fsm.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.fsm.dscatalog.entities.Product;
import com.fsm.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	private long nonExisting = 1000L;
	private long existingId = 1L;
	private long countTotalProducts;
	
	@Autowired
	ProductRepository repository;
		
	@BeforeEach
	public void setUp() throws Exception {
		existingId = 1L;
		nonExisting = 1000l;
		countTotalProducts = 25L;
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		
		product.setId(null);
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts+1, product.getId());
	}
	
	@Test
	public void findByIdShouldReturnOptinalNotEmptyWhenIdExists() {
		
		Optional<Product> entity = repository.findById(existingId);
		
		Assertions.assertTrue(entity.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnOptinalEmptyWhenIdDoesNotExists() {
		
		Optional<Product> entity = repository.findById(nonExisting);
		
		Assertions.assertTrue(entity.isEmpty());
	}
	
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		Optional<Product> entity = repository.findById(existingId);
		
		Assertions.assertTrue(entity.isEmpty());		
	}
	
	@Test
	public void deleteShouldEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExisting);
		});
		
	}
	

	
}
