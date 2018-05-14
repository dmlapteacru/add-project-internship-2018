package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.AdvancedFilter;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.model.dto.CustomerDto;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.service.*;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
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
        model.addAttribute("customerName", currentCustomer.getFullName());
        model.addAttribute("activeContracts",
                contractService.countByCustomerAndStatus(currentCustomer.getId(), ContractStatus.ACTIVE));
        model.addAttribute("signedContracts",
                contractService.countByCustomerAndStatus(currentCustomer.getId(), ContractStatus.SIGNED_BY_CUSTOMER));
        model.addAttribute("unsignedContracts",
                contractService.countByCustomerAndStatus(currentCustomer.getId(), ContractStatus.UNSIGNED));
        model.addAttribute("filterActiveContracts", new AdvancedFilter(ContractStatus.ACTIVE));
        model.addAttribute("filterSignedContracts", new AdvancedFilter(ContractStatus.SIGNED_BY_CUSTOMER));
        model.addAttribute("filterUnsignedContracts", new AdvancedFilter(ContractStatus.UNSIGNED));

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
        return "redirect:/customer/home";
    }

    @GetMapping(value = "contracts")
    public String getContractsPage(Model model) {

        int currentCustomerId = userUtil.getCurrentCustomer().getId();
        model.addAttribute("contractList", contractService.getAllByCustomerId(currentCustomerId));
        model.addAttribute("customerId", currentCustomerId);
        model.addAttribute("companyId", 0);
        model.addAttribute("productId", 0);
        model.addAttribute("statusListForFilter", Arrays.asList(ContractStatus.values()));
        model.addAttribute("filter", new AdvancedFilter());

        return "contract/contractListPage";

    }

    @PostMapping(value = "contracts/filtered")
    public String getCustomerContractsFiltered(@ModelAttribute(name = "filter") AdvancedFilter filter, Model model) {

        int currentCustomerId = userUtil.getCurrentCustomer().getId();
        model.addAttribute("contractList", contractService.getAllByCustomerIdFiltered(currentCustomerId, filter));
        model.addAttribute("customerId", currentCustomerId);
        model.addAttribute("companyId", 0);
        model.addAttribute("productId", 0);
        model.addAttribute("statusListForFilter", Arrays.asList(ContractStatus.values()));
        model.addAttribute("filter", filter);

        return "contract/contractListPage";
    }

    @GetMapping(value = "services")
    public String getProductList(Model model) {

        Customer currentCustomer = userUtil.getCurrentCustomer();
        model.addAttribute("categoryList", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("customerId", currentCustomer.getId());
        model.addAttribute("customerName", currentCustomer.getFullName());
        model.addAttribute("ownerType", "customer");
        model.addAttribute("filter", new AdvancedFilter());

        return "product/productListPage";
    }

    @PostMapping(value = "services/filtered")
    public String getProductListFiltered(@ModelAttribute(name = "filter") AdvancedFilter filter, Model model) {

        Customer currentCustomer = userUtil.getCurrentCustomer();
        model.addAttribute("categoryList", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllFiltered(filter));
        model.addAttribute("customerId", currentCustomer.getId());
        model.addAttribute("customerName", currentCustomer.getFullName());
        model.addAttribute("ownerType", "customer");
        model.addAttribute("filter", filter);

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
        model.addAttribute("invoices", invoiceService.getInvoiceCustomerViewByCutomerId(currentCustomerId));

        return "invoice/invoiceListPage";
    }

    @GetMapping(value = "bank")
    public String getBankPage(Model model) {
        model.addAttribute("customer", userUtil.getCurrentCustomer());
        return "customer/bankPage";
    }

}
