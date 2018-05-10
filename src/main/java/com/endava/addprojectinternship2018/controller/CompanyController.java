package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.dao.ContractDao;
import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.CompanyDto;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "home")
    public String showCompanyPage(Model model) {
        model.addAttribute("company", userUtil.getCurrentCompany());
        return "company/homePage";
    }

    @GetMapping(value = "contracts")
    public String showCompanyContracts(@ModelAttribute(name = "errorMessage") String errorMessage, Model model) {

        int currentCompanyId = userUtil.getCurrentCompany().getId();
        List<Contract> contractList = contractService.getAllByCompanyId(currentCompanyId);

        model.addAttribute("contractList", contractList);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("customerId", 0);
        model.addAttribute("companyId", currentCompanyId);
        model.addAttribute("productId", 0);

        return "contract/contractListPage";
    }

    @GetMapping(value = "invoices")
    public String showInvoicesByContractId(Model model) {

        int currentCompanyId = userUtil.getCurrentCompany().getId();
        List<Invoice> invoices = invoiceService.getInvoicesByCompanyId(currentCompanyId);
        model.addAttribute("companyId", currentCompanyId);
        model.addAttribute("invoices", invoices);

        return "invoice/invoicesByCompany";
    }

    @GetMapping(value = "services")
    public String showProductsByContractId(Model model) {
        Company company = userUtil.getCurrentCompany();
        List<Product> products = productService.getAllByCompanyId(company.getId());
        List<Category> categoryList = categoryService.getAllCategory();
        model.addAttribute("contractDto", new ContractDto());
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("products", products);
        model.addAttribute("company", company);
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
        return "redirect:/company";
    }

}
