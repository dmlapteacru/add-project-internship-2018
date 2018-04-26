package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CustomerDao;
import com.endava.addprojectinternship2018.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    public Optional<Customer> getCustomerById(int id) {
        return customerDao.findById(id);
    }

    public Optional<Customer> getCustomerByFullName(String firstName, String lastName) {
        return customerDao.findByFirstNameAndLastName(firstName, lastName);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customerDao.findByEmail(email);
    }

    public Optional<Customer> getCustomerByBankAccount(String bankAccount) {
        return customerDao.findByBankAccount(bankAccount);
    }

    public Optional<Customer> getCustomerByUserId(int id) {
        return customerDao.findByUserId(id);
    }

}
