package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractDao extends JpaRepository<Contract, Integer> {
    Contract findById(int id);
    @Override
    List<Contract> findAll();
    List<Contract> findByCompanyName (String companyName);


}
