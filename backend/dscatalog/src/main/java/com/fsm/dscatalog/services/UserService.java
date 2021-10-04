package com.fsm.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsm.dscatalog.dto.RoleDTO;
import com.fsm.dscatalog.dto.UserDTO;
import com.fsm.dscatalog.dto.UserInsertDTO;
import com.fsm.dscatalog.entities.Role;
import com.fsm.dscatalog.entities.User;
import com.fsm.dscatalog.repositories.RoleRepository;
import com.fsm.dscatalog.repositories.UserRepository;
import com.fsm.dscatalog.services.exception.DatabaseException;
import com.fsm.dscatalog.services.exception.NotFoundException;

@Service
public class UserService {
	@Autowired
	UserRepository repository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(Pageable pageable){
		Page<User> users = repository.findAll(pageable);		
		return users.map( x -> new UserDTO(x));
	}
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> optinalUser = repository.findById(id);
		User entity = optinalUser.orElseThrow(() -> new NotFoundException("entity not found"));
		return new UserDTO(entity);
	}
	
	@Transactional
	public UserDTO insert(UserInsertDTO newUser) {
		User entity = new User();
		
		copyDtoToEntity(newUser,entity);
		entity.setPassword(passwordEncoder.encode(newUser.getPassword()));
		entity = repository.save(entity);
		
		return new UserDTO(entity);
	}
	


	@Transactional
	public UserDTO update(Long id, UserDTO updateUser) {
		
		try {
			User entity = repository.getOne(id);
			copyDtoToEntity(updateUser,entity);
			entity = repository.save(entity);
			
			return new UserDTO(entity);
			
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
	
	private void copyDtoToEntity(UserDTO fromUser, User toUser) {
		toUser.setFirstName(fromUser.getFirstName());
		toUser.setLastName(fromUser.getLastName());
		toUser.setEmail(fromUser.getEmail());
		

		toUser.getRoles().clear();
		
		for (RoleDTO roleDto :  fromUser.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			toUser.getRoles().add(role);
		}
		
		
	}
	
}
