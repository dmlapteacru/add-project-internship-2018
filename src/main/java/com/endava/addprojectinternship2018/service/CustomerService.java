package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CustomerDao;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final UserService userService;

    @Autowired
    public CustomerService(CustomerDao customerDao, UserService userService) {
        this.customerDao = customerDao;
        this.userService = userService;
    }

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

    public Optional<Customer> getCustomerByUserId(int id) {
        return customerDao.findByUserId(id);
    }

    @Transactional
    public void saveCustomer(CustomerDto customerDto) {
        customerDao.save(convertCustomerDtoToCustomer(customerDto));
    }

    @Transactional
    public void save(Customer customer){
        customerDao.save(customer);
    }

    public CustomerDto convertCustomerToCustomerDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setCustomerId(customer.getId());
        customerDto.setEmail(customer.getEmail());
        customerDto.setCountNumber(customer.getCountNumber());
        customerDto.setAccessKey(customer.getAccessKey());
        customerDto.setUserDto(userService.convertUserToUserDto(customer.getUser()));
        return customerDto;
    }

    public Customer convertCustomerDtoToCustomer(CustomerDto customerDto) {
        Customer customer = customerDao.findById(customerDto.getCustomerId())
                .orElseGet(Customer::new);
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
        customer.setCountNumber(customerDto.getCountNumber());
        customer.setAccessKey(customerDto.getAccessKey());
        customer.setUser(userService.convertUserDtoToUser(customerDto.getUserDto()));
        return customer;
    }
}
