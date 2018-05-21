package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CategoryService categoryService;

    private static final Logger LOGGER = Logger.getLogger(RestController.class);

    //  -----   REST Company
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

}
