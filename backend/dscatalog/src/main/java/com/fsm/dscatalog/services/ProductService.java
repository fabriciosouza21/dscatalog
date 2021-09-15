package com.fsm.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsm.dscatalog.dto.CategoryDTO;
import com.fsm.dscatalog.dto.ProductDTO;
import com.fsm.dscatalog.entities.Category;
import com.fsm.dscatalog.entities.Product;
import com.fsm.dscatalog.repositories.CategoryRepository;
import com.fsm.dscatalog.repositories.ProductRepository;
import com.fsm.dscatalog.services.exception.DatabaseException;
import com.fsm.dscatalog.services.exception.NotFoundException;

@Service
public class ProductService {
	@Autowired
	ProductRepository repository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(PageRequest pageRequest){
		Page<Product> categories = repository.findAll(pageRequest);		
		return categories.map( x -> new ProductDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> optinalProduct = repository.findById(id);
		Product entity = optinalProduct.orElseThrow(() -> new NotFoundException("entity not found"));
		return new ProductDTO(entity,entity.getCategories());
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO newProduct) {
		Product entity = new Product();
		
		copyDtoToEntity(newProduct,entity);
		
		entity = repository.save(entity);
		
		return new ProductDTO(entity);
	}
	


	@Transactional
	public ProductDTO update(Long id, ProductDTO updateProduct) {
		
		try {
			Product entity = repository.getOne(id);
			copyDtoToEntity(updateProduct,entity);
			entity = repository.save(entity);
					
			return new ProductDTO(entity);
			
		} catch (EntityNotFoundException e) {
			throw new NotFoundException("not found"+ id);
		}

	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);			
		}
		catch(EmptyResultDataAccessException e) {
			throw new NotFoundException("not found id :"+ id);			
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violetion");
		}
	}
	
	private void copyDtoToEntity(ProductDTO fromProduct, Product toProduct) {
		toProduct.setName(fromProduct.getName());
		toProduct.setDescription(fromProduct.getDescription());
		toProduct.setDate(fromProduct.getDate());
		toProduct.setPrice(fromProduct.getPrice());
		toProduct.setImgUrl(fromProduct.getImgUrl());		
		
		toProduct.getCategories().clear();
		
		for (CategoryDTO categoryDto :  fromProduct.getCategories()) {
			Category category = categoryRepository.getOne(categoryDto.getId());
			toProduct.getCategories().add(category);
		}
		
		
		
		
	}
	
}
