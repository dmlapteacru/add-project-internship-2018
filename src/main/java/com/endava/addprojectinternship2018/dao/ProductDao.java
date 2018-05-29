package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {

    List<Product> findAllByOrderByCompanyName();

    List<Product> findAllByCompanyIdOrderByName(int id);

    Optional<Product> findByNameAndCategoryIdAndCompanyId(String name, int categoryId, int companyId);

    Optional<Product> findById(int id);

    List<Product> findAllByCompanyIdAndPriceBetween(int companyId, double priceFrom, double priceTo);

    List<Product> findAllByPriceBetween(double priceFrom, double priceTo);

    long countByCompanyId(int id);
}
