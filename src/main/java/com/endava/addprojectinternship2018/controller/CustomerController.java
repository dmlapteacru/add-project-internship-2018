package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.model.dto.CustomerDto;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
import com.endava.addprojectinternship2018.service.*;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "customer")
public class CustomerController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ContractService contractService;

    @GetMapping(value = "home")
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
    public String updateProfile(@ModelAttribute("customerDto") @Valid CustomerDto customerDto,
                                BindingResult result,
                                Model model) {
        model.addAttribute("update", true);
        if (result.hasErrors()) {
            return "registration/customer";
        }

        Optional<Customer> foundCustomer = customerService.getCustomerByEmail(customerDto.getEmail());
        if (foundCustomer.isPresent()) {
            if (foundCustomer.get().getId() != customerDto.getCustomerId()) {
                result.rejectValue("email", "email.error", "Email is not unique");
                return "registration/customer";
            }
        }

        customerService.saveCustomer(customerDto);
        return "redirect:/customer";
    }

    @GetMapping(value = "contracts")
    public String getContractsPage(@ModelAttribute(name = "errorMessage") String errorMessage, Model model) {

        int currentCustomerId = userUtil.getCurrentCustomer().getId();
        List<Contract> contractList = contractService.getAllByCustomerId(currentCustomerId);

        model.addAttribute("contractList", contractList);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("customerId", currentCustomerId);
        model.addAttribute("companyId", 0);
        model.addAttribute("productId", 0);
        return "contract/contractListPage";

    }

    @GetMapping(value = "services")
    public String getProductsPage(Model model) {

        Customer currentCustomer = userUtil.getCurrentCustomer();
        List<Product> productList = productService.getAllProducts();
        List<Category> categoryList = categoryService.getAllCategory();
        model.addAttribute("contractDto", new ContractDto());
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("products", productList);
        model.addAttribute("customerId", currentCustomer.getId());
        model.addAttribute("customerName", currentCustomer.getFullName());

        return "product/productListPage";
    }

    @GetMapping(value = "services/newcontract")
    public String getProductsPageSignContract(@RequestParam(name = "customerId") int customerId,
                                           @RequestParam(name = "companyId") int companyId,
                                           @RequestParam(name = "productId") int productId,
                                           Model model) {
        ContractDto contractDto = contractService.createNewContractDto(customerId, companyId, productId);
        int currentCustomerId = userUtil.getCurrentCustomer().getId();
        List<Product> productList = productService.getAllProducts();
        List<Category> categoryList = categoryService.getAllCategory();
        model.addAttribute("contractDto", contractDto);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("products", productList);
        model.addAttribute("customerId", currentCustomerId);
        model.addAttribute("update", false);
        return "product/productListPage";
    }

    @GetMapping(value = "invoices")
    public String getInvoicesPage(Model model) {

        int currentCustomerId = userUtil.getCurrentCustomer().getId();
        List<Invoice> invoices = invoiceService.getInvoicesByCustomerId(currentCustomerId);
        model.addAttribute("invoices", invoices);

        return "invoice/invoiceListPage";
    }

    @GetMapping(value = "bank")
    public String getBankPage(Model model) {

        return "customer/bankPage";
    }

}
