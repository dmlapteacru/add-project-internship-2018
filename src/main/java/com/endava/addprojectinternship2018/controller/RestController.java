package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Category;
import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.UserDto;
import com.endava.addprojectinternship2018.service.CategoryService;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @Autowired
    private CategoryService categoryService;

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
    public String setNewPass(@RequestBody @Valid UserDto user){
        userService.changeUserPassword(user);
        return "OK";
    }

    @RequestMapping(value = "/admin/categories", method = RequestMethod.GET)
    public List<Category> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @RequestMapping(value = "/admin/newCategory", method = RequestMethod.PUT)
    public String saveNewCategory(@RequestBody Category category){
        categoryService.saveCategory(category);
        return "OK";
    }

    @RequestMapping(value = "/admin/deleteCategory/{id}", method = RequestMethod.DELETE)
    public String deleteCategory(@PathVariable Integer id){
        categoryService.deleteCategory(id);
        return "OK";
    }
}
