package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractDao extends JpaRepository<Contract, Integer> {

    Contract findById(int id);

    List<Contract> findAll();

    List<Contract> findByCompanyName (String companyName);

    List<Contract> findByCustomerId (int id);

}

