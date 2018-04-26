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


//  -----   REST Company
    @GetMapping("/rest/getCompanyByEmail/{name}")
    public Company getCompanyByEmail(@PathVariable String name){
        return companyService.getCompanyByEmail(name).get();
    }

    @GetMapping("/rest/getCompanyById/{id}")
    public Company getCompanyById(@PathVariable int id){
        return companyService.getCompanyById(id).get();
    }

    @GetMapping("/rest/getCompanyByName/{companyName}")
    public Company getCompanyByUsername(@PathVariable String companyName){
        return companyService.getCompanyByName(companyName).get();
    }

//  -------------------------------------------

//

//
//    @GetMapping("rest/customerById/{id}")
//    public Customer getCustomerById(@PathVariable int id){
//        return customerService.getCustomerById(id);
//    }

//    @GetMapping("rest/customerByName/{name}")
//    public Optional<Customer> getCustomerByNameSurname(String name){
//        return customerService.getCustomerByName(name);
//    }



}
