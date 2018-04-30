package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<Product> findAll();

    List<Product> findAllByCategoryId(int id);

    List<Product> findAllByCompanyId(int id);

    Optional<Product> findById(int id);

}
