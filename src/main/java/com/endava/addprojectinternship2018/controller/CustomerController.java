package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.service.InvoiceService;
import com.endava.addprojectinternship2018.service.user.UserService;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping(value = "")
    public String getHomePage(Model model) {
        if (UserUtil.getCurrentCustomer() == null) {
            return "error";
        }
        model.addAttribute("customer", UserUtil.getCurrentCustomer());
        return "customer/homePage";
    }

    @GetMapping(value = "contracts")
    public String getContractsPage(Model model) {

        Customer currentCustomer = getCurrentCustomer();
        if (currentCustomer == null) {
            return "customer/error";
        }
        model.addAttribute("customer", currentCustomer);

        List<Contract> contracts = contractService.getContractsByCustomerId(currentCustomer.getId());
        model.addAttribute("customerContracts", contracts);

        return "customer/contractsPage";
    }

    @GetMapping(value = "invoices")
    public String getInvoicesPage(Model model) {
        if (getCurrentCustomer() == null) {
            return "customer/error";
        }
        List<Invoice> invoices = invoiceService.getInvoicesByCustomerId(getCurrentCustomer().getId());
        model.addAttribute("customer", invoices);
        return "customer/invoicesPage";
    }

    @GetMapping(value = "bank")
    public String getBankPage(Model model) {
        return "customer/bankPage";
    }

    private Customer getCurrentCustomer() {
        Customer result = null;
        Optional<User> userOptional = userService.getUserByUsername(getPrincipal());
        if (userOptional.isPresent()) {
            Optional<Customer> customerOptional = customerService.getCustomerByUserId(userOptional.get().getId());
            if (customerOptional.isPresent()) {
                result = customerOptional.get();
            }
        }
        return result;
    }

    private String getPrincipal() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}
