package com.fsm.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsm.dscatalog.entities.Category;
import com.fsm.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	CategoryRepository repository;
	
	public List<Category> findAll(){
		List<Category> categories = repository.findAll();
		return categories;
	}
	
	
}
