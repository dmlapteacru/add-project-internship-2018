package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(value = "company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping(value = "")
    public String showCompanyPage(){
        return "company/homePage";
    }

    @GetMapping(value = "contracts")
    public String showCompanyContracts(Model model, Authentication authentication){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        int userId = auth.get;

        //todo  to verify additionally

//        Optional<Company> company =  companyService.getCompanyByUserId();
//        if (!company.isPresent()){
//            model.addAttribute("error", "you have some errors");
//            return "company/error";
//        }
       model.addAttribute("contracts", contractService
               .getAllContracts());
        return "company/contractsPage";
    }

    @GetMapping(value = "prices")
    public String showCompanyPriceList(Model model, Authentication authentication){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

//        Optional<Company> company =  companyService.getCompanyByUsername(authentication.getName());
//        Optional<Company> company =  companyService.getCompanyByUsername(name);
//
//        if (!company.isPresent()){
//
//            model.addAttribute("error", "You have some errors");
//            return "company/error";
//        }
        model.addAttribute("prices", "These are our prices");
        return "company/pricesPage";
    }

    @GetMapping(value = "invoices")
    public String showInvoicesByContractId(Model model){
        model.addAttribute("invoices", invoiceService.getAllInvoiceByContractId(1));
        return "company/invoicesByContractPage";
    }




}
