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
public class CustomerService  {

    @Autowired
    CustomerDao customerDao;

    public List<Customer> getAllCustomers(){
        return customerDao.findAll();
    }

    public Optional<Customer> getCustomerById(String id){
        return customerDao.findById(id);
    }

    public Optional<Customer> getCustomerByName(String f_name, String l_name){
        return customerDao.findByFirstNameAndLastName(f_name, l_name);
    }

    public Optional<Customer> getCustomerByBankAccount(String b_account){
        return customerDao.findByBankAccount(b_account);
    }
}
