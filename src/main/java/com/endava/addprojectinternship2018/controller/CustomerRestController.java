package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.service.CustomerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "customerRest")
public class CustomerRestController {

    private final CustomerService customerService;

    private static final Logger LOGGER = Logger.getLogger(CustomerRestController.class);

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/getAllFullNames")
    public Map<Integer, String> getAllCustomers() {

        return customerService.getAllCustomers()
                .stream()
                .collect(Collectors
                        .toMap(Customer::getId, Customer::getFullName, (a, b) -> b));

    }

}
