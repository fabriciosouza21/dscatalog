package com.fsm.dscatalog.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fsm.dscatalog.dto.UserDTO;
import com.fsm.dscatalog.dto.UserInsertDTO;
import com.fsm.dscatalog.dto.UserUpdateDTO;
import com.fsm.dscatalog.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
	@Autowired
	UserService service;
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable){
		Page<UserDTO> users = service.findAll(pageable);
		return ResponseEntity.ok().body(users);	
		
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id){
		UserDTO users = service.findById(id);
		return ResponseEntity.ok().body(users);	
		
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO newUser){
		UserDTO users = service.insert(newUser);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
				buildAndExpand(users.getId()).toUri();
		return ResponseEntity.created(uri).body(users);	
		
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update( @PathVariable Long id,@Valid @RequestBody UserUpdateDTO updateUser){
		UserDTO users = service.update(id,updateUser);
		return ResponseEntity.ok().body(users);	
		
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();	
		
	}
	
}
