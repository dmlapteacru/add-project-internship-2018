package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {
}
