package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.*;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.service.*;
import com.endava.addprojectinternship2018.util.UserUtil;
import com.endava.addprojectinternship2018.validation.ErrorMessage;
import com.endava.addprojectinternship2018.validation.ValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    @Qualifier("bank_ip")
    private String bankIP;

    @Autowired
    private RestTemplate restTemplate;

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
    private ObjectError error;


    @GetMapping("/rest/getAllCompanies")
    public List<Company> getAllCompanies(){
        return companyService.getAllCompanies();
    }


//  -----   REST Company
    @GetMapping("/rest/getCompanyByEmail/{name}")
    public Company getCompanyByEmail(@PathVariable String name){
        return companyService.getCompanyByEmail(name).get();
    }

    @GetMapping("/rest/getCompanyById/{id}")
    public Company getCompanyById(@PathVariable int id){
        return companyService.getCompanyById(id).get();
    }

    @GetMapping("/rest/getCompanyByName/{companyName}")
    public Company getCompanyByUsername(@PathVariable String companyName){
        return companyService.getCompanyByName(companyName).get();
    }

    @RequestMapping(value = "/admin/services", method = GET)
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @RequestMapping(value = "/newUserPassword", method = RequestMethod.PUT)
    public String setNewPass(@RequestBody UserDto user){
        userService.changeUserPassword(user);
        return "OK";
    }

    @RequestMapping(value = "/admin/categories", method = GET)
    public List<Category> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @RequestMapping(value = "/categories", method = GET)
    public List<Category> getAllCategories(){
        return categoryService.getAllCategory();
    }

    @RequestMapping(value = "/admin/newCategory", method = RequestMethod.PUT)
    public String saveNewCategory(@RequestBody Category category){
        categoryService.saveCategory(category);
        return "OK";
    }

    @RequestMapping(value = "/admin/deleteCategory/{id}", method = RequestMethod.DELETE)
    public String deleteCategory(@PathVariable Integer id){
        categoryService.deleteCategory(id);
        return "OK";
    }

    @RequestMapping(value = "/contract/newContract", method = RequestMethod.POST)
    public @ResponseBody ValidationResponse saveNewContract(@Valid @RequestBody ContractDtoTest contractDtoTest,
                                                            BindingResult bindingResult) {

        ValidationResponse response = new ValidationResponse();
        response.setStatus("SUCCESS");
        final List<ErrorMessage> errorMessageList = new ArrayList<>();

        if (bindingResult.hasErrors()) {
            response.setStatus("FAIL");
            bindingResult.getFieldErrors().stream()
                    .map(fieldError -> new ErrorMessage(fieldError.getField(), fieldError.getDefaultMessage()))
                    .forEach(errorMessageList::add);
        } else if (contractDtoTest.getExpireDate().isBefore(contractDtoTest.getIssueDate())) {
            response.setStatus("FAIL");
            errorMessageList.add(new ErrorMessage("expireDate", "can not be more than issue date"));
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
        }

        return response;
    }

    @RequestMapping(value = "/contract/deleteContract", method = RequestMethod.POST)
    public String deleteContract(@RequestParam(name = "contractId") int contractId) {
        return contractService.deleteContract(contractId);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword(@RequestBody PasswordToken passwordToken){
        passwordTokenService.save(passwordToken);
        return "OK";
    }
    @RequestMapping(value = "/company/newService", method = RequestMethod.POST)
    public String addNewService(@RequestBody ProductDtoTest product){
        productService.save(product);
        return "OK";
    }

    @RequestMapping(value = "/admin/messages", method = GET)
    public List<AdminMessage> showMessages(){
        return adminMessageService.getAllMessages();
    }

    @RequestMapping(value = "/admin/messages/unread", method = GET)
    public List<AdminMessage> showMessagesByStatusUnread(){
        return adminMessageService.getAllMessagesByStatusUnread();
    }
    @RequestMapping(value = "/admin/messages/read", method = GET)
    public List<AdminMessage> showMessagesByStatusRead(){
        return adminMessageService.getAllMessagesByStatusRead();
    }

    @RequestMapping(value = "/message/send", method = RequestMethod.POST)
    public String addNewMessage(@RequestBody AdminMessage adminMessage){
        adminMessageService.save(adminMessage);
        return "OK";
    }
    @RequestMapping(value = "/admin/message/changeStatus/{id}", method = RequestMethod.PUT)
    public String changeMessageStatus(@PathVariable int id){
        adminMessageService.changeMessageStatus(id);
        return "OK";
    }
    @RequestMapping(value = "/admin/message/delete/{id}", method = RequestMethod.DELETE)
    public String deleteMessage(@PathVariable int id){
        adminMessageService.deleteById(id);
        return "OK";
    }


    @RequestMapping(value = "/bankAccount/create", method = POST)
    public ResponseEntity<UserBankAccountDto> newAccount(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> request = new HttpEntity<>("param", headers);
        UserBankAccountDto response = restTemplate.postForObject( bankIP +"/bankaccount/create", request , UserBankAccountDto.class );
        userBankAccountService.save(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/balance", method = POST)
    public String getBalance(){
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
        headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        HttpEntity<String> request = new HttpEntity<>("param", headers);
        String response = restTemplate.postForObject( bankIP +"/bankaccount/balance", request , String.class );
        return response;
    }

    @RequestMapping(value = "/bankAccount/addmoney", method = POST)
    public String addMoney(@RequestParam Double sum){
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
        headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        Map<String, Double> body = new HashMap<>();
        body.put("sum", sum);
        HttpEntity<Map> request = new HttpEntity<>(body, headers);
        String response = restTemplate.postForObject( bankIP +"/bankaccount/addmoney", request , String.class );
        return response;
    }

    @RequestMapping(value = "/bankAccount/payinvoice", method = POST)
    public String payInvoice(@RequestBody PaymentDto paymentDto){
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
        headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        paymentDto.setCorrespondentCount(companyService.getCompanyByInvoiceId(paymentDto.getCorrespondentCount().intValue()).getCountNumber());
        HttpEntity<PaymentDto> request = new HttpEntity<>(paymentDto, headers);
        String response = restTemplate.postForObject( bankIP +"/sendmoney", request , String.class );
        return response;
    }

    @RequestMapping(value = "/bankAccount/statement", method = POST)
    public String getStatement(){
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
        headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        Map<String, String> dates = new HashMap<>();
        dates.put("Date", "2018-01-01");
        dates.put("DateTo", "2018-05-12");
        HttpEntity<Map> request = new HttpEntity<>(dates, headers);
        String response = restTemplate.postForObject( bankIP +"/statement/statement", request , String.class );
        return response;
    }
}
