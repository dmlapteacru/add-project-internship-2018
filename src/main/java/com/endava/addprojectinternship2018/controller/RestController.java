package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.model.dto.ContractDtoTest;
import com.endava.addprojectinternship2018.model.dto.ProductDtoTest;
import com.endava.addprojectinternship2018.model.dto.UserDto;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.service.*;
import com.endava.addprojectinternship2018.validation.ErrorMessage;
import com.endava.addprojectinternship2018.validation.ValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
public class RestController {

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

    @RequestMapping(value = "/admin/services", method = RequestMethod.GET)
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @RequestMapping(value = "/newUserPassword", method = RequestMethod.PUT)
    public String setNewPass(@RequestBody UserDto user){
        userService.changeUserPassword(user);
        return "OK";
    }

    @RequestMapping(value = "/admin/categories", method = RequestMethod.GET)
    public List<Category> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
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

    @RequestMapping(value = "/admin/resetPassword", method = RequestMethod.POST)
    public String resetPassword(@RequestBody PasswordToken passwordToken){
        passwordTokenService.save(passwordToken);
        return "OK";
    }
    @RequestMapping(value = "/company/newService", method = RequestMethod.POST)
    public String addNewService(@RequestBody ProductDtoTest product){
        productService.save(product);
        return "OK";
    }

    @RequestMapping(value = "/admin/messages", method = RequestMethod.GET)
    public List<AdminMessage> showMessages(){
        return adminMessageService.getAllMessages();
    }

    @RequestMapping(value = "/admin/messages/unread", method = RequestMethod.GET)
    public List<AdminMessage> showMessagesByStatusUnread(){
        return adminMessageService.getAllMessagesByStatusUnread();
    }
    @RequestMapping(value = "/admin/messages/read", method = RequestMethod.GET)
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
}
