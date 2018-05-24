package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.CompanyDtoLight;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.validation.ErrorMessage;
import com.endava.addprojectinternship2018.validation.ValidationResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "companyRest")
public class CompanyRestController {

    private static final Logger LOGGER = Logger.getLogger(CompanyRestController.class);

    private final CompanyService companyService;

    @Autowired
    public CompanyRestController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping(value = "/getNameById")
    public String getCompanyNameById(@RequestParam(name = "companyId") int companyId) {
        return companyService.getCompanyById(companyId)
                .map(Company::getName).orElse("");
    }

    @GetMapping(value = "getAllIdAndName")
    public List<CompanyDtoLight> getAllCompany() {
        return companyService.getAllCompanies().stream()
                .map(company -> new CompanyDtoLight(company.getId(), company.getName()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

}
