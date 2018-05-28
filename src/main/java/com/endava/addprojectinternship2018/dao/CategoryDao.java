package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {

    Optional<Category> findByName(String name);

    Optional<Category> findById(int id);

}
