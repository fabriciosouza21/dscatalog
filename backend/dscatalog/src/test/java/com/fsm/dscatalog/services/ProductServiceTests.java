package com.fsm.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fsm.dscatalog.repositories.ProductRepository;
import com.fsm.dscatalog.services.exception.NotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	private long existingId;
	private long nonExistingId;
	
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(NotFoundException.class).when(repository).deleteById(nonExistingId);
		
		
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		
		Assertions.assertDoesNotThrow(()-> {
			service.delete(existingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdDoesNotExists() {
		
		Assertions.assertThrows(NotFoundException.class,()-> {
			service.delete(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
		
	}
}
