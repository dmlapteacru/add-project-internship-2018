package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.CompanyDto;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.InvoiceService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.service.user.UserService;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    @GetMapping(value = "")
    public String showCompanyPage(Model model) {
        if (userUtil.getCurrentCompany() == null) {
            return "company/error";
        }
        model.addAttribute("company", userUtil.getCurrentCompany());
        return "company/homePage";
    }

    @GetMapping(value = "contracts")
    public String showCompanyContracts(Model model) {
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

    @GetMapping(value = "products")
    public String showProductsByContractId(Model model) {
        Company company = userUtil.getCurrentCompany();
        List<Product> products = productService.getAllByCompanyId(company.getId());
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            productDtoList.add(productService.convertProductToProductDto(product));
        }
        model.addAttribute("products", productDtoList);
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
