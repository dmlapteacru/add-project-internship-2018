package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.service.*;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final CompanyService companyService;
    private final CategoryService categoryService;

    private UserUtil userUtil;

    @Autowired
    public RestController(CompanyService companyService, CategoryService categoryService) {
        this.companyService = companyService;
        this.categoryService = categoryService;
    }

    @Autowired
    public void setUserUtil(UserUtil userUtil) {
        this.userUtil = userUtil;
    }

    @GetMapping("/rest/getCompanyByEmail/{name}")
    public Company getCompanyByEmail(@PathVariable String name) {
        return companyService.getCompanyByEmail(name).get();
    }

    @GetMapping("/rest/getCompanyByName/{companyName}")
    public Company getCompanyByUsername(@PathVariable String companyName) {
        return companyService.getCompanyByName(companyName).get();
    }

    @RequestMapping(value = "/categories", method = GET)
    public List<Category> getAllCategories() {
        return categoryService.getAllCategory();
    }

    @RequestMapping(value = "/status/dev", method = GET)
    public ResponseEntity<?> devOpsCheckStatus(){
        return new ResponseEntity<>("STATUS CHECKED ;) I AM A TEAPOT",HttpStatus.I_AM_A_TEAPOT);
    }

    @RequestMapping(value = "/getToken", method = GET)
    public String getSocketToken(){
        return userUtil.getCurrentUser().getSocketToken();
    }
}
