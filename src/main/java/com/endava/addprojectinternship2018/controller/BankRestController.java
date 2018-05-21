package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.dto.PaymentDto;
import com.endava.addprojectinternship2018.model.dto.StatementDateReqDto;
import com.endava.addprojectinternship2018.model.dto.StatementDto;
import com.endava.addprojectinternship2018.model.dto.UserBankAccountDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.InvoiceService;
import com.endava.addprojectinternship2018.service.UserBankAccountService;
import com.endava.addprojectinternship2018.service.UserService;
import com.endava.addprojectinternship2018.util.UserUtil;
import com.endava.addprojectinternship2018.validation.Validator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class BankRestController {
    @Autowired
    @Qualifier("bank_ip")
    private String bankIP;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserBankAccountService userBankAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private Validator validator;

    private static final Logger LOGGER = Logger.getLogger(com.endava.addprojectinternship2018.controller.BankRestController.class);

    @RequestMapping(value = "/bankAccount/create", method = POST)
    public ResponseEntity<UserBankAccountDto> newAccount() throws ConnectException {
        HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> request = new HttpEntity<>("param", headers);
        UserBankAccountDto response = restTemplate.postForObject(bankIP + "/bankaccount/create", request, UserBankAccountDto.class);
        userBankAccountService.save(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/balance", method = POST)
    public ResponseEntity<?> getBalance() throws ConnectException {
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        try {
            headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
            headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        } catch (NullPointerException ex){
            LOGGER.error(ex.getMessage());
        }
        HttpEntity<String> request = new HttpEntity<>("param", headers);
        String response;
        try {
            response = restTemplate.postForObject(bankIP + "/bankaccount/balance", request, String.class);
        } catch (Exception ex){
            LOGGER.error(ex.getMessage());
            return new ResponseEntity<>("No connection", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
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
        try {
            restTemplate.postForObject(bankIP + "/bankaccount/addmoney", request, String.class);
        } catch (Exception ex){
            return new ResponseEntity<>("No connection with bank.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Money added.", HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/payinvoice", method = POST)
    public ResponseEntity<?> payInvoice(@RequestParam(name = "bal") Double balance, @RequestBody PaymentDto paymentDto, BindingResult error) {
        validator.validateInvoicePayment(balance, paymentDto, error);
        if (error.hasErrors()) {
            return new ResponseEntity<>(error.getFieldError().getCode(), HttpStatus.NOT_FOUND);
        }
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
            headers.add("accessKey", userBankAccountDto.getAccessKey().toString());

        int idInvoice = paymentDto.getCorrespondentCount().intValue();
        paymentDto.setDescription(paymentDto.getDescription() + invoiceService.setInvoiceDescription(paymentDto.getCorrespondentCount().intValue()).getFullDescription());
        paymentDto.setCorrespondentCount(companyService.getCompanyByInvoiceId(paymentDto.getCorrespondentCount().intValue()).getCountNumber());
        HttpEntity<PaymentDto> request = new HttpEntity<>(paymentDto, headers);
        System.out.println(paymentDto);
        ResponseEntity<Object> response;
        try {
           response = restTemplate.postForEntity(bankIP + "/sendmoney/sendmoney", request, Object.class);
        } catch (Exception ex){
            return new ResponseEntity<>("No connection with bank.", HttpStatus.BAD_REQUEST);
        }

        if (response.getStatusCode().equals(HttpStatus.OK)){
            invoiceService.setInvoiceAsPaid(idInvoice);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/payinvoice/bulk", method = POST)
    public ResponseEntity<?> payInvoice(@RequestParam(name = "bal") Double balance, @RequestBody List<PaymentDto> paymentDtoList, BindingResult error) {
        System.out.println(paymentDtoList);
        validator.validateBulkInvoicePayment(balance, paymentDtoList, error);
        if (error.hasErrors()) {
            return new ResponseEntity<>("Not enough money.", HttpStatus.BAD_REQUEST);
        }
        UserBankAccountDto userBankAccountDto = userService.getUserBankAccountByUsername(userUtil.getCurrentUser().getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("countNumber", userBankAccountDto.getCountNumber().toString());
        headers.add("accessKey", userBankAccountDto.getAccessKey().toString());
        List<Integer> idList = new ArrayList<>();
        for (PaymentDto p : paymentDtoList
                ) {
            idList.add(p.getCorrespondentCount().intValue());
            p.setDescription(p.getDescription() +
                    invoiceService.setInvoiceDescription(p.getCorrespondentCount().intValue())
                            .getFullDescription());
            p.setCorrespondentCount(companyService.getCompanyByInvoiceId(p.getCorrespondentCount().intValue())
                    .getCountNumber());
        }
        HttpEntity<List<PaymentDto>> request = new HttpEntity<>(paymentDtoList, headers);
        ResponseEntity<Object> response;
        try {
            response = restTemplate.postForEntity(bankIP + "/sendmoney/bulkpayment", request, Object.class);
        } catch (Exception ex){
            return new ResponseEntity<>("No connection with bank.", HttpStatus.BAD_REQUEST);
        }

        if (response.getStatusCode().equals(HttpStatus.OK)){
            invoiceService.setInvoiceBulkAsPaid(idList);
        }
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
        ResponseEntity<Object> response;
        try {
            response = restTemplate.exchange(bankIP + "/statement/statement", HttpMethod.POST, request, Object.class);
        } catch (Exception ex){
            return new ResponseEntity<>("No connection with bank.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }
}
