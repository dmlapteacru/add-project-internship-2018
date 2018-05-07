package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Category;
import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.service.CategoryService;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ContractService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "service")
public class ProductController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "create")
    public String getProductPageForCreate(Model model) {

        Company currentCompany = userUtil.getCurrentCompany();
        ProductDto productDto = new ProductDto();
        productDto.setCompany(currentCompany);
        model.addAttribute("productDto", productDto);

        List<Category> categoryList = categoryService.getAllCategory();
        model.addAttribute("categoryList", categoryList);

        model.addAttribute("update", false);
        return "product/productPage";

    }

    @GetMapping(value = "update")
    public String getProductPageForUpdate(@RequestParam(name = "productId") int productId,
                                           Model model) {

        return "product/productPage";
    }

    @GetMapping(value = "delete")
    public String deleteProduct(@RequestParam(name = "productId") int productId,
                                 Model model) {
        return "";
    }

    @PostMapping(value = "save")
    public String saveProduct(@ModelAttribute(name = "productDto") ProductDto productDto,
                               BindingResult result, Model model) {

        String resultString;
        if (productDto.getProductId() == 0) {
            model.addAttribute("update", false);
            resultString = "redirect:/service/create?success";
        } else {
            model.addAttribute("update", true);
            resultString = "redirect:/service/update?productId=" + productDto.getProductId() + "&success";
        }
        if (result.hasErrors()) {
            return "product/productPage";
        }

        Optional<Product> productOptional = productService.getByNameAndCompanyId(
                productDto.getName(), productDto.getCompany().getId());
        if (productOptional.isPresent()) {
            result.rejectValue("name", "The name of product is not unique!");
            return "contract/contractPage";
        }

        productService.saveProduct(productDto);

        return resultString;
    }

}
