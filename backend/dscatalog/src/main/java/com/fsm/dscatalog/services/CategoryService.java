package com.fsm.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsm.dscatalog.dto.CategoryDTO;
import com.fsm.dscatalog.entities.Category;
import com.fsm.dscatalog.repositories.CategoryRepository;
import com.fsm.dscatalog.services.exception.DatabaseException;
import com.fsm.dscatalog.services.exception.NotFoundException;

@Service
public class CategoryService {
	@Autowired
	CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAll(Pageable pageable){
		Page<Category> categories = repository.findAll(pageable);		
		return categories.map( x -> new CategoryDTO(x));
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> optinalCategory = repository.findById(id);
		Category entity = optinalCategory.orElseThrow(() -> new NotFoundException("entity not found"));
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO newCategory) {
		Category entity = new Category();
		entity.setName(newCategory.getName());
		entity = repository.save(entity);
		
		return new CategoryDTO(entity);
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO updateCategory) {
		
		try {
			Category entity = repository.getOne(id);
			entity.setName(updateCategory.getName());
			entity = repository.save(entity);
					
			return new CategoryDTO(entity);
			
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
	
	
}
