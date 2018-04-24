package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyDao extends JpaRepository<Company, Integer> {

     List<Company> findAll();
     Company findById(int id);
     Company findByName(String name);
     Company findByEmail(String email);


}
