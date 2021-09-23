package com.fsm.dscatalog.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.fsm.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{


}
