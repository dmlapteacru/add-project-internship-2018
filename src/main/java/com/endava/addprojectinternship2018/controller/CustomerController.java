package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.AdvancedFilter;
import com.endava.addprojectinternship2018.model.dto.CustomerDto;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.service.*;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping(value = "customer")
public class CustomerController {

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserService userService;

    private final CustomerService customerService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ContractService contractService;
    private final InvoiceService invoiceService;

    private static final Logger LOGGER = Logger.getLogger(CustomerController.class);

    @Autowired
    public CustomerController(CustomerService customerService, ProductService productService,
                              CategoryService categoryService, ContractService contractService,
                              InvoiceService invoiceService) {
        this.customerService = customerService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.contractService = contractService;
        this.invoiceService = invoiceService;
    }

    @GetMapping(value = "home")
    public String getHomePage(Model model) {

        Customer currentCustomer = userUtil.getCurrentCustomer();
        int currentCustomerId = currentCustomer.getId();
        model.addAttribute("customerName", currentCustomer.getFullName());
        model.addAttribute("activeContracts",
                contractService.countByCustomerAndStatus(currentCustomerId, ContractStatus.ACTIVE));
        model.addAttribute("onePartSignedContracts",
                contractService.countByCustomerAndStatus(currentCustomerId, ContractStatus.SIGNED_BY_CUSTOMER));
        model.addAttribute("anotherPartSignedContracts",
                contractService.countByCompanyAndStatus(currentCustomerId, ContractStatus.SIGNED_BY_COMPANY));
        model.addAttribute("unsignedContracts",
                contractService.countByCustomerAndStatus(currentCustomerId, ContractStatus.UNSIGNED));
        model.addAttribute("filterActiveContracts", new AdvancedFilter(ContractStatus.ACTIVE));
        model.addAttribute("filterSignedContracts", new AdvancedFilter(ContractStatus.SIGNED_BY_CUSTOMER));
        model.addAttribute("filterUnsignedContracts", new AdvancedFilter(ContractStatus.UNSIGNED));

        model.addAttribute("totalServices", productService.countAll());

        model.addAttribute("filterSentInvoices", new AdvancedFilter(InvoiceStatus.SENT));
        model.addAttribute("filterPaidInvoices", new AdvancedFilter(InvoiceStatus.PAID));
        model.addAttribute("filterOverdueInvoices", new AdvancedFilter(InvoiceStatus.OVERDUE));
        model.addAttribute("sentInvoices", invoiceService.countByCustomerIdAndStatus(currentCustomerId, InvoiceStatus.SENT));
        model.addAttribute("paidInvoices", invoiceService.countByCustomerIdAndStatus(currentCustomerId, InvoiceStatus.PAID));
        model.addAttribute("overdueInvoices", invoiceService.countByCustomerIdAndStatus(currentCustomerId, InvoiceStatus.OVERDUE));

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

        LOGGER.info(String.format("Customer %s:%s updated profile", customerDto.getCustomerId(), customerDto.getLastName()));

        return "redirect:/customer/home";
    }

    @GetMapping(value = "contracts")
    public String getContractsPage(Model model) {

        int currentCustomerId = userUtil.getCurrentCustomer().getId();
        model.addAttribute("contractList", contractService.getAllByCustomerId(currentCustomerId));
        model.addAttribute("customerId", currentCustomerId);
        model.addAttribute("companyId", 0);
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

    @GetMapping(value = "invoices")
    public String getInvoicesPage(Model model) {

        int currentCustomerId = userUtil.getCurrentCustomer().getId();
        model.addAttribute("invoices", invoiceService.getAllByCustomerId(currentCustomerId));
        model.addAttribute("filter", new AdvancedFilter());

        model.addAttribute("statusListForFilter", invoiceService.getStatusesForCustomer());

        return "invoice/invoiceListPage";
    }

    @PostMapping(value = "invoices/filtered")
    public String getInvoicesFiltered(@ModelAttribute(name = "filter") AdvancedFilter filter, Model model) {

        int currentCustomerId = userUtil.getCurrentCustomer().getId();
        model.addAttribute("invoices", invoiceService.getInvoicesByCustomerIdFiltered(currentCustomerId, filter));
        model.addAttribute("filter", filter);

        model.addAttribute("statusListForFilter", invoiceService.getStatusesForCustomer());

        return "invoice/invoiceListPage";
    }

    @GetMapping(value = "bank")
    public String getBankPage(Model model) {
        model.addAttribute("customer", userUtil.getCurrentCustomer());
        return "customer/bankPage";
    }

    @GetMapping(value = "notifications")
    public String showNotifications(){
        return "customer/notifications";
    }
}
