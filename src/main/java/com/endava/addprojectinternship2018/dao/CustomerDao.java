package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerDao extends JpaRepository <Customer, Integer> {

    List<Customer> findAll();
    Customer findById(int id);
    Customer findByFirstNameAndLastName(String f_name, String L_name);
    Customer findByBankAccount(String bank_acc);

}
