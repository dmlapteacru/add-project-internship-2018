package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractDao extends JpaRepository<Contract, Integer> {

    Optional<Contract> findById(int id);

    List<Contract> findAll();

    List<Contract> findAllByCompanyName(String companyName);

    List<Contract> findAllByCompanyId(int id);

    List<Contract> findAllByCustomerId(int id);

    List<Contract> findAllByProductId(int productId);

}

