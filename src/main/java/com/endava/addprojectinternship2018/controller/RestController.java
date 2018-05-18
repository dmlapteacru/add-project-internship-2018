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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    @Qualifier("bank_ip")
    private String bankIP;

    @Autowired
    private RestTemplate restTemplate;
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
    private PasswordTokenService passwordTokenService;

    @Autowired
    private AdminMessageService adminMessageService;

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

    @RequestMapping(value = "/admin/services", method = RequestMethod.GET)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @RequestMapping(value = "/newUserPassword", method = RequestMethod.PUT)
    public String setNewPass(@RequestBody UserDto user) {
        userService.changeUserPassword(user);
        return "OK";
    }

    @RequestMapping(value = "/admin/categories", method = RequestMethod.GET)
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public List<Category> getAllCategories() {
        return categoryService.getAllCategory();
    }

    @RequestMapping(value = "/admin/newCategory", method = RequestMethod.PUT)
    public ResponseEntity<?> saveNewCategory(@RequestBody Category category, BindingResult error) {
        validator.validate(category, error);
        if (error.hasErrors()) {
            return new ResponseEntity<>(error.getFieldError().getCode(), HttpStatus.BAD_REQUEST);
        }
        categoryService.saveCategory(category);
        LOGGER.info(String.format("admin has created new category %s:%s", category.getId(), category.getName()));
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/deleteCategory/{id}", method = RequestMethod.DELETE)
    public String deleteCategory(@PathVariable Integer id) {
        LOGGER.info(String.format("admin is trying to delete category with id %s", id));
        String categoryName = categoryService.getCategoryById(id).getName();
        categoryService.deleteCategory(id);
        LOGGER.info(String.format("category %s:%s was deleted", id, categoryName));
        return "OK";
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

    @RequestMapping(value = "/resetPassword", method = POST)
    public String resetPassword(@RequestBody PasswordToken passwordToken) {
        passwordTokenService.save(passwordToken);
        return "OK";
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

    @RequestMapping(value = "/admin/messages", method = RequestMethod.GET)
    public List<AdminMessage> showMessages() {
        return adminMessageService.getAllMessages();
    }

    @RequestMapping(value = "/admin/messages/unread", method = RequestMethod.GET)
    public List<AdminMessage> showMessagesByStatusUnread() {
        return adminMessageService.getAllMessagesByStatusUnread();
    }

    @RequestMapping(value = "/admin/messages/read", method = RequestMethod.GET)
    public List<AdminMessage> showMessagesByStatusRead() {
        return adminMessageService.getAllMessagesByStatusRead();
    }

    @RequestMapping(value = "/message/send", method = POST)
    public String addNewMessage(@RequestBody AdminMessage adminMessage) {
        adminMessageService.save(adminMessage);
        return "OK";
    }

    @RequestMapping(value = "/admin/message/changeStatus/{id}", method = RequestMethod.PUT)
    public String changeMessageStatus(@PathVariable int id) {
        adminMessageService.changeMessageStatus(id);
        return "OK";
    }

    @RequestMapping(value = "/admin/message/changeStatus/read", method = RequestMethod.PUT)
    public String changeMessageStatusOnRead(@RequestBody List<ChangeMessageStatusDto> changeMessageStatusDtoList) {
        adminMessageService.changeMessageStatusOnRead(changeMessageStatusDtoList);
        return "OK";
    }

    @RequestMapping(value = "/admin/message/changeStatus/unread", method = RequestMethod.PUT)
    public String changeMessageStatusOnUnRead(@RequestBody List<ChangeMessageStatusDto> changeMessageStatusDtoList) {
        adminMessageService.changeMessageStatusOnUnRead(changeMessageStatusDtoList);
        return "OK";
    }

    @RequestMapping(value = "/admin/message/delete/{id}", method = RequestMethod.DELETE)
    public String deleteMessage(@PathVariable int id) {
        adminMessageService.deleteById(id);
        return "OK";
    }

    @RequestMapping(value = "/admin/message/bulkDelete", method = RequestMethod.DELETE)
    public String deleteMessage(@RequestBody List<ChangeMessageStatusDto> changeMessageStatusDtoList) {
        adminMessageService.deleteMessages(changeMessageStatusDtoList);
        return "OK";
    }

    @RequestMapping(value = "/admin/changeUserStatus/active", method = POST)
    public String changeUserStatusOnActive(@RequestBody List<ChangeUserStatusDto> changeUserStatusDto) {
        userService.changeUserStatusOnActive(changeUserStatusDto);
        return "OK";
    }

    @RequestMapping(value = "/admin/changeUserStatus/inactive", method = POST)
    public String changeUserStatusOnInactive(@RequestBody List<ChangeUserStatusDto> changeUserStatusDto) {
        userService.changeUserStatusOnInactive(changeUserStatusDto);
        return "OK";
    }

    @RequestMapping(value = "/bankAccount/create", method = POST)
    public ResponseEntity<UserBankAccountDto> newAccount() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> request = new HttpEntity<>("param", headers);
        UserBankAccountDto response = restTemplate.postForObject(bankIP + "/bankaccount/create", request, UserBankAccountDto.class);
        userBankAccountService.save(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/balance", method = POST)
    public String getBalance() {
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
        headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        HttpEntity<String> request = new HttpEntity<>("param", headers);
        String response = restTemplate.postForObject(bankIP + "/bankaccount/balance", request, String.class);
        return response;
    }

    @RequestMapping(value = "/bankAccount/addmoney", method = POST)
    public ResponseEntity<?> addMoney(@RequestParam Double sum) {
        if (sum == null || sum < 0.01) {
            return new ResponseEntity<>("Must be greater than 0.01 MDL.", HttpStatus.BAD_REQUEST);
        }
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
        headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        Map<String, Double> body = new HashMap<>();
        body.put("sum", sum);
        HttpEntity<Map> request = new HttpEntity<>(body, headers);
        restTemplate.postForObject(bankIP + "/bankaccount/addmoney", request, String.class);
        return new ResponseEntity<>("Money added.", HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/payinvoice", method = POST)
    public ResponseEntity<?> payInvoice(@RequestParam(name = "bal") Double balance, @RequestBody PaymentDto paymentDto, BindingResult error) {
        validator.validateInvoicePayment(balance, paymentDto, error);
        if (error.hasErrors()) {
            return new ResponseEntity<>(error.getFieldError().getCode(), HttpStatus.BAD_REQUEST);
        }
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
        headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        invoiceService.setInvoiceAsPaid(paymentDto.getCorrespondentCount().intValue());
        paymentDto.setDescription(paymentDto.getDescription() + invoiceService.setInvoiceDescription(paymentDto.getCorrespondentCount().intValue()).getFullDescription());
        paymentDto.setCorrespondentCount(companyService.getCompanyByInvoiceId(paymentDto.getCorrespondentCount().intValue()).getCountNumber());
        HttpEntity<PaymentDto> request = new HttpEntity<>(paymentDto, headers);
        restTemplate.postForObject(bankIP + "/sendmoney", request, String.class);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/payinvoice/bulk", method = POST)
    public ResponseEntity<?> payInvoice(@RequestParam(name = "bal") Double balance, @RequestBody List<PaymentDto> paymentDtoList, BindingResult error) {
        validator.validateBulkInvoicePayment(balance, paymentDtoList, error);
        if (error.hasErrors()) {
            return new ResponseEntity<>("Not enough money.", HttpStatus.BAD_REQUEST);
        }
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
        headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        for (PaymentDto p : paymentDtoList
                ) {
            invoiceService.setInvoiceAsPaid(p.getCorrespondentCount().intValue());
            p.setDescription(p.getDescription() +
                    invoiceService.setInvoiceDescription(p.getCorrespondentCount().intValue())
                            .getFullDescription());
            p.setCorrespondentCount(companyService.getCompanyByInvoiceId(p.getCorrespondentCount().intValue())
                    .getCountNumber());
        }
//        HttpEntity<PaymentDto> request = new HttpEntity<>(paymentDto, headers);
//        restTemplate.postForObject( bankIP +"/sendmoney", request , String.class );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/statement", method = POST)
    public ResponseEntity<?> getStatement(@RequestBody StatementDateReqDto dateReqDto, BindingResult error) {
        validator.validateStatementDates(dateReqDto, error);
        if (error.hasErrors()) {
            return new ResponseEntity<>(error.getFieldError().getCode(), HttpStatus.BAD_REQUEST);
        }
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
        headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        HttpEntity<StatementDateReqDto> request = new HttpEntity<>(dateReqDto, headers);
        ResponseEntity<List<StatementDto>> response = restTemplate.exchange(bankIP + "/statement/statement", HttpMethod.POST, request, new ParameterizedTypeReference<List<StatementDto>>() {
        });
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    @RequestMapping(value = "servicesRest/pdfExport", method = RequestMethod.GET,
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
