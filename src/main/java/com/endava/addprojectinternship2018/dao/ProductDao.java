package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {

    List<Product> findAll();

    List<Product> findAllByCategoryId(int id);

    List<Product> findAllByCompanyId(int id);

    List<Product> findAllByCompanyIdAndCategoryId(int companyId, int categoryId);

    Optional<Product> findByNameAndCategoryIdAndCompanyId(String name, int categoryId, int companyId);

    Optional<Product> findById(int id);

    Optional<Product> findByNameAndCompanyId(String name, int companyId);

    void deleteById(int id);

    Optional<Product> findByNameAndCategoryId(String name, int categoryId);

}
