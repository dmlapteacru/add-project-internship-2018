package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/rest/getAllCompanies")
    public List<Company> getAllCompanies(){
        return companyService.getAllCompanies();
    }

    @GetMapping("/rest/getCompanyByName/{name}")
    public Optional<Company> getCompanyByUsername(@PathVariable String name){
        return companyService.getCompanyByEmail(name);
    }

    @GetMapping("/rest/getCompanyByName/{id}")
    public Optional<Company> getCompanyById(@PathVariable String id){
        return companyService.getCompanyByEmail(id);
    }

    @GetMapping("/rest/getCompanyByName/{email}")
    public Optional<Company> getCompanyByEmail(@PathVariable String email){
        return companyService.getCompanyByEmail(email);
    }

    @GetMapping("rest/customerById/{id}")
    public Optional<Customer> getCustomerById(@PathVariable String id){
        return customerService.getCustomerById(id);
    }



}
