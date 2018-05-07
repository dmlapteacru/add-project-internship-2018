package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.ProductDtoTest;
import com.endava.addprojectinternship2018.model.dto.UserDto;
import com.endava.addprojectinternship2018.service.*;
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

    @Autowired
    private PasswordTokenService passwordTokenService;

    @Autowired
    private AdminMessageService adminMessageService;


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

    @RequestMapping(value = "/newUserPassword", method = RequestMethod.PUT)
    public String setNewPass(@RequestBody UserDto user){
        userService.changeUserPassword(user);
        return "OK";
    }

    @RequestMapping(value = "/admin/categories", method = RequestMethod.GET)
    public List<Category> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<Category> getAllCategories(){
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

    @RequestMapping(value = "/admin/resetPassword", method = RequestMethod.POST)
    public String resetPassword(@RequestBody PasswordToken passwordToken){
        passwordTokenService.save(passwordToken);
        return "OK";
    }
    @RequestMapping(value = "/company/newService", method = RequestMethod.POST)
    public String addNewService(@RequestBody ProductDtoTest product){
        productService.save(product);
        return "OK";
    }

    @RequestMapping(value = "/admin/messages", method = RequestMethod.GET)
    public List<AdminMessage> showMessages(){
        return adminMessageService.getAllMessages();
    }
    @RequestMapping(value = "/admin/messages/unread", method = RequestMethod.GET)
    public List<AdminMessage> showMessagesByStatusUnread(){
        return adminMessageService.getAllMessagesByStatusUnread();
    }
    @RequestMapping(value = "/admin/messages/read", method = RequestMethod.GET)
    public List<AdminMessage> showMessagesByStatusRead(){
        return adminMessageService.getAllMessagesByStatusRead();
    }

    @RequestMapping(value = "/message/send", method = RequestMethod.POST)
    public String addNewMessage(@RequestBody AdminMessage adminMessage){
        System.out.println("con " + adminMessage);
        adminMessageService.save(adminMessage);
        return "OK";
    }
}
