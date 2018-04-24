package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ContractService contractService;

    @RequestMapping(value = "/")
    public String redirectLogin() {
        return "redirect:/company";
    }

    @GetMapping("/company")
    public String showCompanyPage(){


        return "company";
    }

    @GetMapping("/company/contracts")
    public String showCompanyContracts(Model model){
        model.addAttribute("All contracts", companyService.getAllCompanies());
        return "contracts";
    }



}
