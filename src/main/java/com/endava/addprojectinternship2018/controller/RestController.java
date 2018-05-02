package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.UserRegistrationDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

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

    @RequestMapping(value = "/admin/services", method = RequestMethod.GET)
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @RequestMapping(value = "/admin/newUserPassword", method = RequestMethod.PUT)
    public String setNewPass(@RequestBody @Valid UserRegistrationDto user){
        userService.changeUserPassword(user);
        return "OK";
    }

    @RequestMapping(value = "/admin/newProduct", method = RequestMethod.POST)
    public String saveNewProduct(@RequestBody Product product){
        productService.saveProduct(product);
        return "OK";
    }

    @RequestMapping(value = "/admin/deleteProduct/{id}", method = RequestMethod.DELETE)
    public String saveNewProduct(@PathVariable Integer id){
        productService.deleteProduct(id);
        return "OK";
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
