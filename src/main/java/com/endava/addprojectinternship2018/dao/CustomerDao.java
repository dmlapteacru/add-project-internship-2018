package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerDao extends JpaRepository <Customer, Integer> {

    List<Customer> findAll();

    Optional <Customer> findById(int id);

    Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByUserId(int id);

    Optional<Customer> findByCountNumber(long count);

}
