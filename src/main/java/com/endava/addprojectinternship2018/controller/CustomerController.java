package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "")
    public String getHomePage(Model model) {
        return "customer/mainPage";
    }

    @GetMapping(value = "contracts")
    public String getContractsPage(Model model) {
        return "contractsPage";
    }

    @GetMapping(value = "invoices")
    public String getInvoicesPage(Model model) {
        return "invoicesPage";
    }

    @GetMapping(value = "bank")
    public String getBankPage(Model model) {
        return "customer/bank";
    }
}
