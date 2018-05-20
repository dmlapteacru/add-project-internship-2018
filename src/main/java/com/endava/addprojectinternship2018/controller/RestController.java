package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.*;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.service.*;
import com.endava.addprojectinternship2018.util.UserUtil;
import com.endava.addprojectinternship2018.validation.ErrorMessage;
import com.endava.addprojectinternship2018.validation.ValidationResponse;
import com.endava.addprojectinternship2018.validation.Validator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    @Qualifier("bank_ip")
    private String bankIP;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebSocketDistributeService webSocketService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserBankAccountService userBankAccountService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private Validator validator;

    private static final Logger LOGGER = Logger.getLogger(RestController.class);


    //  -----   REST Company
    @GetMapping("/rest/getCompanyByEmail/{name}")
    public Company getCompanyByEmail(@PathVariable String name) {
        return companyService.getCompanyByEmail(name).get();
    }

    @GetMapping(value = "companyRest/getNameById")
    public String getCompanyNameById(@RequestParam(name = "companyId") int companyId) {
        return companyService.getCompanyById(companyId)
                .map(Company::getName).orElse("");
    }

    @GetMapping("/rest/getCompanyByName/{companyName}")
    public Company getCompanyByUsername(@PathVariable String companyName) {
        return companyService.getCompanyByName(companyName).get();
    }


    @RequestMapping(value = "/categories", method = GET)
    public List<Category> getAllCategories() {
        return categoryService.getAllCategory();
    }


    @PostMapping(value = "/contractRest/newContract")
    public @ResponseBody
    ValidationResponse saveNewContract(@Valid @RequestBody ContractDtoTest contractDtoTest,
                                       BindingResult bindingResult) {

        String currentUsername = userUtil.getCurrentUser().getUsername();
        LOGGER.info(String.format("%s: creating new contract...", currentUsername));

        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");
        final List<ErrorMessage> errorMessageList = new ArrayList<>();

        if (contractDtoTest.getIssueDate() == null) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_issueDate", "cannot be null"));
            response.setErrorMessageList(errorMessageList);
            return response;
        }

        if (contractDtoTest.getExpireDate() == null) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_expireDate", "cannot be null"));
            response.setErrorMessageList(errorMessageList);
            return response;
        }

        if (contractDtoTest.getExpireDate().isBefore(contractDtoTest.getIssueDate())) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_expireDate", "cannot be more than issue date"));
        }

        if (contractDtoTest.getProductId() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_productSelect", "product must be selected"));
        }

        if (contractDtoTest.getCustomerId() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_customerSelect", "customer must be selected"));
        }

        if (contractDtoTest.getSum() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_sum", "cannot be null"));
        }

        if (bindingResult.hasErrors()) {
            response.setStatus("FAIL");
            bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorMessage(fieldError.getField(), fieldError.getDefaultMessage()))
                    .forEach(errorMessageList::add);
        }

        Optional<Contract> optionalContract = contractService.getByCustomerIdCompanyIdProductId(
                contractDtoTest.getCustomerId(),
                contractDtoTest.getCompanyId(),
                contractDtoTest.getProductId());
        if (optionalContract.isPresent()) {
            if (optionalContract.get().getExpireDate().isAfter(LocalDate.now())) {
                response.setStatus("FAIL");
                errorMessageList.add(new ErrorMessage("new_contract", "Duplicate contract exists!"));
            }
        }

        response.setErrorMessageList(errorMessageList);

        if (response.getStatus().equals("SUCCESS")) {
            ContractDto contractDto = new ContractDto();
            contractDto.setSelectedProduct(productService.getById(contractDtoTest.getProductId()));
            contractDto.setSelectedCustomer(customerService.getCustomerById(contractDtoTest.getCustomerId()).get());
            contractDto.setSelectedCompany(companyService.getCompanyById(contractDtoTest.getCompanyId()).get());
            contractDto.setSum(contractDtoTest.getSum());
            contractDto.setIssueDate(contractDtoTest.getIssueDate());
            contractDto.setExpireDate(contractDtoTest.getExpireDate());
            contractDto.setStatus(ContractStatus.valueOf(contractDtoTest.getStatus()));
            contractService.saveContract(contractDto);

            LOGGER.info(String.format("%s: creating new contract... SUCCESS", currentUsername));
        }

        if (!errorMessageList.isEmpty()) {
            for (ErrorMessage errorMessage : errorMessageList) {
                LOGGER.warn(String.format("%s: contract validation error (field:%s message:%s)", currentUsername, errorMessage.getFieldName(), errorMessage.getMessage()));
            }
        }

        return response;
    }

    @GetMapping(value = "/customerRest/getAllFullNames")
    public Map<Integer, String> getAllCustomers() {

        return customerService.getAllCustomers()
                .stream()
                .collect(Collectors
                        .toMap(Customer::getId, Customer::getFullName, (a, b) -> b));

    }

    @GetMapping(value = "/productRest/getAllByCompanyId")
    public Map<Integer, String> getAllByCompanyId(@RequestParam(name = "companyId") int companyId) {

        return productService.getAllByCompanyId(companyId)
                .stream()
                .collect(Collectors
                        .toMap(Product::getId, Product::getName, (a, b) -> b));

    }

    @GetMapping(value = "/productRest/getPriceById")
    public double getPriceById(@RequestParam(name = "productId") int productId) {
        return productService.getById(productId).getPrice();
    }

    @RequestMapping(value = "/contractRest/deleteContract", method = POST)
    public String deleteContract(@RequestParam(name = "contractId") int contractId) {
        LOGGER.info(String.format("%s: attempt to delete contract: %s", userUtil.getCurrentUser().getUsername(), contractId));
        return contractService.deleteContract(contractId);
    }

    @PostMapping(value = "/contractRest/signContract")
    public String signContract(@RequestParam(name = "contractId") int contractId) {
        return contractService.signContract(contractId);
    }

    @RequestMapping(value = "/companyRest/newService", method = POST)
    public @ResponseBody
    ValidationResponse saveNewService(@RequestBody @Valid ProductDtoTest productDtoTest,
                                      BindingResult bindingResult) {

        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");
        final List<ErrorMessage> errorMessageList = new ArrayList<>();

        if (productDtoTest.getCategoryId() == 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("select_category", "must not be empty"));
        }

        if (productDtoTest.getName() == null || productDtoTest.getName().isEmpty()) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_name", "must not be empty"));
        }

        if (productDtoTest.getName().matches("(<\\s*script\\s*>)|(alert\\s*\\(\\s*\\))")) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_name", "contains illegal characters"));
        }

        if (productDtoTest.getDescription().matches("(<\\s*script\\s*>)|(alert\\s*\\(\\s*\\))")) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_desc", "contains illegal characters"));
        }


        if (productDtoTest.getPrice() <= 0) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("new_service_price", "must be more than 0"));
        }

        Optional<Product> optionalProduct = productService.getByNameAndCategoryIdAndCompanyId(
                productDtoTest.getName(),
                productDtoTest.getCategoryId(),
                productDtoTest.getCompanyId());
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
            productService.save(productDtoTest);
        }

        return response;
    }

    @RequestMapping(value = "servicesRest/pdfExport", method = GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> citiesReport() throws IOException {

        ByteArrayInputStream bis = productService.getPriceList();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=ServicePriceList.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
