package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.AdvancedFilter;
import com.endava.addprojectinternship2018.model.dto.CompanyDto;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.service.*;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "company")
public class CompanyController {

    @Autowired
    private UserUtil userUtil;

    private final CompanyService companyService;
    private final ContractService contractService;
    private final InvoiceService invoiceService;
    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public CompanyController(CompanyService companyService, ContractService contractService, InvoiceService invoiceService, UserService userService, ProductService productService, CategoryService categoryService) {
        this.companyService = companyService;
        this.contractService = contractService;
        this.invoiceService = invoiceService;
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = "home")
    public String showCompanyPage(Model model) {

        Company currentCompany = userUtil.getCurrentCompany();
        int currentCompanyId = currentCompany.getId();
        model.addAttribute("companyName", currentCompany.getName());
        model.addAttribute("activeContracts",
                contractService.countByCompanyAndStatus(currentCompanyId, ContractStatus.ACTIVE));
        model.addAttribute("onePartSignedContracts",
                contractService.countByCompanyAndStatus(currentCompanyId, ContractStatus.SIGNED_BY_COMPANY));
        model.addAttribute("anotherPartSignedContracts",
                contractService.countByCompanyAndStatus(currentCompanyId, ContractStatus.SIGNED_BY_CUSTOMER));
        model.addAttribute("unsignedContracts",
                contractService.countByCompanyAndStatus(currentCompanyId, ContractStatus.UNSIGNED));
        model.addAttribute("filterActiveContracts", new AdvancedFilter(ContractStatus.ACTIVE));
        model.addAttribute("filterSignedContracts", new AdvancedFilter(ContractStatus.SIGNED_BY_COMPANY));
        model.addAttribute("filterUnsignedContracts", new AdvancedFilter(ContractStatus.UNSIGNED));

        model.addAttribute("totalServices", productService.countByCompanyId(currentCompany.getId()));

        model.addAttribute("filterIssuedInvoices", new AdvancedFilter(InvoiceStatus.ISSUED));
        model.addAttribute("filterSentInvoices", new AdvancedFilter(InvoiceStatus.SENT));
        model.addAttribute("filterPaidInvoices", new AdvancedFilter(InvoiceStatus.PAID));
        model.addAttribute("filterOverdueInvoices", new AdvancedFilter(InvoiceStatus.OVERDUE));
        model.addAttribute("issuedInvoices", invoiceService.countByCompanyIdAndStatus(currentCompanyId, InvoiceStatus.ISSUED));
        model.addAttribute("sentInvoices", invoiceService.countByCompanyIdAndStatus(currentCompanyId, InvoiceStatus.SENT));
        model.addAttribute("paidInvoices", invoiceService.countByCompanyIdAndStatus(currentCompanyId, InvoiceStatus.PAID));
        model.addAttribute("overdueInvoices", invoiceService.countByCompanyIdAndStatus(currentCompanyId, InvoiceStatus.OVERDUE));

        return "company/homePage";
    }

    @GetMapping(value = "contracts")
    public String getCompanyContracts(Model model) {

        int currentCompanyId = userUtil.getCurrentCompany().getId();
        String currentCompanyName = userUtil.getCurrentCompany().getName();

        model.addAttribute("contractList", contractService.getAllByCompanyId(currentCompanyId));
        model.addAttribute("customerId", 0);
        model.addAttribute("companyId", currentCompanyId);
        model.addAttribute("companyName", currentCompanyName);
        model.addAttribute("status", InvoiceStatus.ISSUED);
        model.addAttribute("statusListForFilter", Arrays.asList(ContractStatus.values()));
        model.addAttribute("filter", new AdvancedFilter());

        return "contract/contractListPage";
    }

    @PostMapping(value = "contracts/filtered")
    public String getCompanyContractsFiltered(@ModelAttribute(name = "filter") AdvancedFilter filter, Model model) {

        int currentCompanyId = userUtil.getCurrentCompany().getId();
        String currentCompanyName = userUtil.getCurrentCompany().getName();
        List<Contract> contractList = contractService.getAllByCompanyIdFiltered(currentCompanyId, filter);
        List<ContractStatus> contractStatusList = Arrays.asList(ContractStatus.values());

        model.addAttribute("contractList", contractList);
        model.addAttribute("customerId", 0);
        model.addAttribute("companyId", currentCompanyId);
        model.addAttribute("companyName", currentCompanyName);
        model.addAttribute("status", InvoiceStatus.ISSUED);
        model.addAttribute("statusListForFilter", contractStatusList);
        model.addAttribute("filter", filter);

        return "contract/contractListPage";
    }

    @GetMapping(value = "invoices")
    public String showInvoicesByContractId(Model model) {

        int currentCompanyId = userUtil.getCurrentCompany().getId();
        List<Invoice> invoices = invoiceService.getInvoicesByCompanyId(currentCompanyId);
        model.addAttribute("companyId", currentCompanyId);
        model.addAttribute("invoices", invoices);

        model.addAttribute("filter", new AdvancedFilter());
        model.addAttribute("statusListForFilter", Arrays.asList(InvoiceStatus.values()));

        return "invoice/invoicesByCompany";
    }

    @PostMapping(value = "invoices/filtered")
    public String getInvoicesFiltered(@ModelAttribute(name = "filter") AdvancedFilter filter, Model model) {

        int currentCompanyId = userUtil.getCurrentCompany().getId();
        model.addAttribute("companyId", currentCompanyId);
        model.addAttribute("invoices", invoiceService.getInvoicesByCompanyIdFiltered(currentCompanyId, filter));

        model.addAttribute("filter", filter);
        model.addAttribute("statusListForFilter", Arrays.asList(InvoiceStatus.values()));

        return "invoice/invoicesByCompany";
    }

    @GetMapping(value = "services")
    public String getProductsByCompany(Model model) {

        Company company = userUtil.getCurrentCompany();
        model.addAttribute("ownerType", "company");
        model.addAttribute("companyId", company.getId());
        model.addAttribute("products", productService.getAllByCompanyId(company.getId()));
        model.addAttribute("categoryList", categoryService.getAllCategory());
        model.addAttribute("filter", new AdvancedFilter());

        return "product/productListPage";
    }

    @PostMapping(value = "services/filtered")
    public String getProductsByCompanyFiltered(@ModelAttribute(name = "filter") AdvancedFilter filter, Model model) {

        Company company = userUtil.getCurrentCompany();
        model.addAttribute("ownerType", "company");
        model.addAttribute("companyId", company.getId());
        model.addAttribute("products", productService.getAllByCompanyIdFiltered(company.getId(), filter));
        model.addAttribute("categoryList", categoryService.getAllCategory());
        model.addAttribute("filter", filter);

        return "product/productListPage";
    }

    @GetMapping(value = "profile")
    public String getProfilePage(Model model) {
        CompanyDto companyDto = companyService.convertCompanyToCompanyDto(userUtil.getCurrentCompany());
        companyDto.setUserDto(userService.convertUserToUserDto(userUtil.getCurrentUser()));
        model.addAttribute("companyDto", companyDto);
        model.addAttribute("update", true);
        return "registration/company";
    }

    @PostMapping(value = "updateProfile")
    public String updateProfile(@ModelAttribute("companyDto") @Valid CompanyDto companyDto,
                                BindingResult result,
                                Model model) {

        model.addAttribute("update", true);

        if (result.hasErrors()) {
            return "registration/company";
        }

        Optional<Company> foundCompany = companyService.getCompanyByEmail(companyDto.getEmail());
        if (foundCompany.isPresent()) {
            if (foundCompany.get().getId() != companyDto.getCompanyId()) {
                result.rejectValue("email", "email.error", "Email is not unique");
                return "registration/company";
            }
        }

        companyService.saveCompany(companyDto);
        return "redirect:/company/home";
    }

    @GetMapping(value = "bank")
    public String getBankPage(Model model) {
        model.addAttribute("company", userUtil.getCurrentCompany());
        return "company/bankPage";
    }

}
