package com.fsm.dscatalog.services;

import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fsm.dscatalog.dto.CategoryDTO;
import com.fsm.dscatalog.dto.ProductDTO;
import com.fsm.dscatalog.entities.Category;
import com.fsm.dscatalog.entities.Product;
import com.fsm.dscatalog.repositories.CategoryRepository;
import com.fsm.dscatalog.repositories.ProductRepository;
import com.fsm.dscatalog.services.exception.DatabaseException;
import com.fsm.dscatalog.services.exception.NotFoundException;
import com.fsm.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO productDto;
	
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4;
		product = Factory.createProduct();
		category = Factory.createCategory();
		page = new PageImpl<>(List.of(product));
		productDto = Factory.createProductDTO();
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
				
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	
	}
	
	
	@Test
	public void findAllPagedShouldReturnPage() {
		PageRequest pageRequest = PageRequest.of(0, 5);
		
		Page<ProductDTO> page = service.findAll(pageRequest);
		
		Assertions.assertNotNull(page);
		Mockito.verify(repository).findAll(pageRequest);
	}
	
	@Test
	public void findByIdShouldReturnProductDtoWhenIdExists() {
		
		ProductDTO productDTO = service.findById(existingId);
		
		Assertions.assertNotNull(productDTO);
		Mockito.verify(repository).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(NotFoundException.class,()->{
			service.findById(nonExistingId);
		});
		Mockito.verify(repository).findById(nonExistingId);
	}
	
	
	@Test
	public void updateShouldReturnProductDtoWhenIdExists() {
		
		ProductDTO product = service.update(existingId,productDto);
		
		Assertions.assertNotNull(product);
	}
	
	@Test
	public void updateShouldThrowNotFoundExceptionWhenIdDoesNotExists() {

		Assertions.assertThrows(NotFoundException.class,()->{
			service.update(nonExistingId,productDto);
		});

	}
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class,()-> {
			service.delete(dependentId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
		
	}
	

	@Test
	public void deleteShouldThrowDoNothingWhenIdExists() {
		
		
		Assertions.assertDoesNotThrow(()-> {
			service.delete(existingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
		
	}
	
	@Test
	public void deleteShouldThrowNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(NotFoundException.class,()-> {
			service.delete(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
		
	}
}
