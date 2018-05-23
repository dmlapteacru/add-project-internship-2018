package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.CompanyDtoLight;
import com.endava.addprojectinternship2018.model.dto.ProductDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.ProductService;
import com.endava.addprojectinternship2018.validation.ErrorMessage;
import com.endava.addprojectinternship2018.validation.ValidationResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "companyRest")
public class CompanyRestController {

    private static final Logger LOGGER = Logger.getLogger(CompanyRestController.class);

    private final CompanyService companyService;
    private final ProductService productService;

    @Autowired
    public CompanyRestController(CompanyService companyService, ProductService productService) {
        this.companyService = companyService;
        this.productService = productService;
    }

    @GetMapping(value = "/getNameById")
    public String getCompanyNameById(@RequestParam(name = "companyId") int companyId) {
        return companyService.getCompanyById(companyId)
                .map(Company::getName).orElse("");
    }

    @RequestMapping(value = "/newService", method = POST)
    public @ResponseBody
    ValidationResponse saveNewService(@RequestBody @Valid ProductDto productDto,
                                      BindingResult bindingResult) {

        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");
        final List<ErrorMessage> errorMessageList = new ArrayList<>();

        if (productDto.getCategoryId() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("select_category", "must not be empty"));
        }

        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_name", "must not be empty"));
        }

        if (productDto.getName().matches("(<\\s*script\\s*>)|(alert\\s*\\(\\s*\\))")) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_name", "contains illegal characters"));
        }

        if (productDto.getDescription().matches("(<\\s*script\\s*>)|(alert\\s*\\(\\s*\\))")) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_desc", "contains illegal characters"));
        }

        if (productDto.getPrice() <= 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_price", "must be more than 0"));
        }

        Optional<Product> optionalProduct = productService.getByNameAndCategoryIdAndCompanyId(
                productDto.getName(),
                productDto.getCategoryId(),
                productDto.getCompanyId());
        if (optionalProduct.isPresent()) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_name", "service name exists"));
        }

        if (bindingResult.hasErrors()) {
            response.setStatus("FAIL");
            bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorMessage(fieldError.getField(), fieldError.getDefaultMessage()))
                    .forEach(errorMessageList::add);
        }

        response.setErrorMessageList(errorMessageList);

        if (response.getStatus().equals("SUCCESS")) {
            productService.save(productDto);
        }

        return response;
    }

    @GetMapping(value = "getAllIdAndName")
    public List<CompanyDtoLight> getAllCompany() {
        return companyService.getAllCompanies().stream()
                .map(company -> new CompanyDtoLight(company.getId(), company.getName()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

}
