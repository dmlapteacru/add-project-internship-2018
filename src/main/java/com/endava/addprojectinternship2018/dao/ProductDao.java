package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
    void deleteById(Integer id);

    @Override
    Optional<Product> findById(Integer id);
}
