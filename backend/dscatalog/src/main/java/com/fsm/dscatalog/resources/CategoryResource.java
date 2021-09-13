package com.fsm.dscatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fsm.dscatalog.entities.Category;





@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	
	@GetMapping
	public ResponseEntity<List<Category>> findAll(){
		List<Category> categories = new ArrayList<>();
		categories.add(new Category(1L,"eletronic"));
		categories.add(new Category(2L,"books"));
		return ResponseEntity.ok().body(categories);
		
		
	}
}