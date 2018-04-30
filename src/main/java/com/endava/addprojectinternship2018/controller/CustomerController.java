package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.Enums.Role;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.model.dto.CustomerDto;
import com.endava.addprojectinternship2018.model.dto.UserDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.service.InvoiceService;
import com.endava.addprojectinternship2018.service.user.UserService;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "customer")
public class CustomerController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "")
    public String getHomePage(Model model) {
        Customer currentCustomer = userUtil.getCurrentCustomer();
        if (currentCustomer == null) {
            return "error";
        }
        model.addAttribute("customer", currentCustomer);
        return "customer/homePage";
    }

    @GetMapping(value = "profile")
    public String getProfilePage(Model model) {
        CustomerDto customerDto = customerService.convertCustomerToCustomerDto(userUtil.getCurrentCustomer());
        customerDto.setUserDto(userService.convertUserToUserDto(userUtil.getCurrentUser()));
        model.addAttribute("customerDto", customerDto);
        model.addAttribute("update", true);
        return "registration/customer";
    }

    @PostMapping(value = "updateProfile")
    public String updateProfile(@ModelAttribute("user") @Valid CustomerDto customerDto,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            model.addAttribute("update", true);
            return "registration/customer";
        }

        customerService.saveCustomer(customerDto);
        return "redirect:/customer";
    }

    @GetMapping(value = "contracts")
    public String getContractsPage(Model model) {

        Customer currentCustomer = userUtil.getCurrentCustomer();
        if (currentCustomer == null) {
            return "customer/error";
        }
        model.addAttribute("customer", currentCustomer);

        List<Contract> contracts = contractService.getContractsByCustomerId(currentCustomer.getId());
        List<ContractDto> contractDtoList = new ArrayList<>();
        for (Contract contract : contracts) {
            contractDtoList.add(contractService.convertContractToContractDto(contract));
        }
        model.addAttribute("customerContracts", contractDtoList);

        return "customer/contractsPage";
    }

    @GetMapping(value = "invoices")
    public String getInvoicesPage(Model model) {
        Customer currentCustomer = userUtil.getCurrentCustomer();
        if (currentCustomer == null) {
            return "customer/error";
        }
        model.addAttribute("customer", currentCustomer);

        List<Invoice> invoices = invoiceService.getInvoicesByCustomerId(currentCustomer.getId());
        model.addAttribute("customerInvoices", invoices);

        return "customer/invoicesPage";
    }

    @GetMapping(value = "companies")
    public String getCompaniesPage(Model model) {

        Customer currentCustomer = userUtil.getCurrentCustomer();
        if (currentCustomer == null) {
            return "customer/error";
        }
        model.addAttribute("customer", currentCustomer);

        List<Company> companyList = companyService.getAllCompanies();
        model.addAttribute("companies", companyList);

        return "customer/companiesPage";
    }

    @GetMapping(value = "bank")
    public String getBankPage(Model model) {
        return "customer/bankPage";
    }

}
