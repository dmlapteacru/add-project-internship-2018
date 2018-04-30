package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.InvoiceService;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "company")
public class CompanyController {

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping(value = "")
    public String showCompanyPage(Model model) {
        if (userUtil.getCurrentCompany() == null){
            return "company/error";
        }
            model.addAttribute("company", userUtil.getCurrentCompany());
        return "company/homePage";
    }

    @GetMapping(value = "contracts")
    public String showCompanyContracts(Model model, Authentication authentication) {
        Company company = userUtil.getCurrentCompany();
        model.addAttribute("contracts", contractService
                .getContractsByCompanyName(company.getName()));
        return "company/contractsPage";
    }

    @GetMapping(value = "invoices")
    public String showInvoicesByContractId(Model model) {
        Company company = userUtil.getCurrentCompany();
        model.addAttribute("invoices", invoiceService
                .getInvoicesByCompany(company.getName()));
        return "company/invoicesByCompany";
    }

}
