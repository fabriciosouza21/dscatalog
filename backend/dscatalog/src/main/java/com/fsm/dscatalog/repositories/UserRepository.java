package com.fsm.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fsm.dscatalog.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String string);

}
