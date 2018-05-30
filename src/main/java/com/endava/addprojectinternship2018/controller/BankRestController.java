package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.exception.NoBankAccountException;
import com.endava.addprojectinternship2018.exception.NoBankConnectionException;
import com.endava.addprojectinternship2018.model.dto.*;
import com.endava.addprojectinternship2018.service.InvoiceService;
import com.endava.addprojectinternship2018.service.UserBankAccountService;
import com.endava.addprojectinternship2018.service.UserService;
import com.endava.addprojectinternship2018.util.EncryptionUtils;
import com.endava.addprojectinternship2018.util.UserUtil;
import com.endava.addprojectinternship2018.validation.Validator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import static org.springframework.http.HttpStatus.*;

@RestController
public class BankRestController {

    private UserUtil userUtil;
    private final String bankIP;
    private final RestTemplate restTemplate;
    private final UserBankAccountService userBankAccountService;
    private final UserService userService;
    private final InvoiceService invoiceService;
    private final Validator validator;
    private final EncryptionUtils encryptionUtils;

    private static final Logger LOGGER = Logger.getLogger(com.endava.addprojectinternship2018.controller.BankRestController.class);

    @Autowired
    public BankRestController(@Qualifier("bank_ip") String bankIP, RestTemplate restTemplate,
                              UserBankAccountService userBankAccountService, UserService userService,
                              InvoiceService invoiceService, Validator validator, EncryptionUtils encryptionUtils) {
        this.bankIP = bankIP;
        this.restTemplate = restTemplate;
        this.userBankAccountService = userBankAccountService;
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.validator = validator;
        this.encryptionUtils = encryptionUtils;
    }

    @Autowired
    public void setUserUtil(UserUtil userUtil) {
        this.userUtil = userUtil;
    }

    @PostMapping(value = "/bankAccount/create")
    public ResponseEntity<UserBankAccountDto> newAccount() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity request = new HttpEntity<>("param", headers);
        UserBankAccountDto response = restTemplate.postForObject(bankIP + "/bankaccount/create", request, UserBankAccountDto.class);
        userBankAccountService.save(response);
        return new ResponseEntity<>(OK);
    }

    @PostMapping(value = "/bankAccount/balance")
    public ResponseEntity<?> getBalanceResponseEntity() {
        try {
            return new ResponseEntity<>(getBalance(), OK);
        } catch (NoBankAccountException | NoBankConnectionException ex) {
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }
    }

    @PostMapping(value = "/bankAccount/addmoney")
    public ResponseEntity<?> addMoney(@RequestParam Double sum) {

        if (sum == null || sum < 0.01) {
            LOGGER.warn("add money validation error");
            return new ResponseEntity<>("Sum must be greater than 0.01 MDL.", BAD_REQUEST);
        }
        HttpHeaders headers;
        try {
            headers = initHeaders();
        } catch (NoBankAccountException | NoBankConnectionException ex) {
            LOGGER.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), BAD_REQUEST);
        }
        Map<String, Double> body = new HashMap<>();
        body.put("sum", sum);
        try {
            HttpEntity<Map> request = new HttpEntity<>(body, headers);
            restTemplate.postForObject(bankIP + "/bankaccount/addmoney", request, String.class);
        } catch (Exception ex) {
            LOGGER.error("add money error: " + ex.getMessage());
            return new ResponseEntity<>("No connection with bank", BAD_REQUEST);
        }

        LOGGER.info(userUtil.getCurrentUser().getUsername() + " added money successful");
        return new ResponseEntity<>("Money added", OK);
    }

    @PostMapping(value = "/bankAccount/payinvoice")
    public ResponseEntity<?> payInvoice(@RequestBody int invoiceId) {

        String currentCustomerName = userUtil.getCurrentCustomer().getFullName();

        HttpHeaders headers;

        double balance;
        try {
            balance = getBalance();
            headers = initHeaders();
        } catch (NoBankAccountException | NoBankConnectionException ex) {
            return new ResponseEntity<>(ex.getMessage(), PRECONDITION_FAILED);
        }

        PaymentDto paymentDto = invoiceService.createPaymentDto(invoiceId);
        List<PaymentDto> paymentDtoList = new ArrayList<>();
        paymentDtoList.add(paymentDto);
        List<String> errors = validator.validateInvoicePayment(balance, paymentDtoList);
        ResponseEntity<?> errorsResponse = checkErrors(currentCustomerName, errors);
        if (errorsResponse != null) return errorsResponse;

        ResponseEntity<Object> response;
        try {
            HttpEntity<PaymentDto> request = new HttpEntity<>(paymentDto, headers);
            response = restTemplate.postForEntity(bankIP + "/sendmoney/sendmoney", request, Object.class);
        } catch (Exception ex) {
            LOGGER.error(currentCustomerName + ": " + ex.getMessage());
            return new ResponseEntity<>("No connection with bank", BAD_REQUEST);
        }

        if (response.getStatusCode() == OK) {
            LOGGER.info(currentCustomerName + " paid invoice: " + paymentDto);
            invoiceService.setInvoiceAsPaid(invoiceId);
        }

        return new ResponseEntity<>(OK);
    }

    private ResponseEntity<?> checkErrors(String currentCustomerName, List<String> errors) {
        if (!errors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String error : errors) {
                LOGGER.error(currentCustomerName + " payment validation error: " + error);
                sb.append(error + "\n");
            }
            return new ResponseEntity<>(sb.toString(), PRECONDITION_FAILED);
        }
        return null;
    }

    @PostMapping(value = "/bankAccount/payinvoice/bulk")
    public ResponseEntity<?> payInvoice(@RequestBody List<Integer> invoiceIdList) {

        String currentCustomerName = userUtil.getCurrentCustomer().getFullName();

        HttpHeaders headers;

        double balance;
        try {
            balance = getBalance();
            headers = initHeaders();
        } catch (NoBankAccountException | NoBankConnectionException ex) {
            return new ResponseEntity<>(ex.getMessage(), PRECONDITION_FAILED);
        }

        List<PaymentDto> paymentDtoList = new ArrayList<>();
        for (Integer invoiceId : invoiceIdList) {
            paymentDtoList.add(invoiceService.createPaymentDto(invoiceId));
        }

        List<String> errors = validator.validateInvoicePayment(balance, paymentDtoList);
        ResponseEntity<?> errorsResponse = checkErrors(currentCustomerName, errors);
        if (errorsResponse != null) return errorsResponse;

        ResponseEntity<Object> response;
        try {
            HttpEntity<List<PaymentDto>> request = new HttpEntity<>(paymentDtoList, headers);
            response = restTemplate.postForEntity(bankIP + "/sendmoney/bulkpayment", request, Object.class);
        } catch (Exception ex) {
            LOGGER.error(currentCustomerName + " bulk payment: " + ex.getMessage());
            return new ResponseEntity<>("No connection with bank", PRECONDITION_FAILED);
        }
        if (response.getStatusCode().equals(OK)) {
            LOGGER.info(currentCustomerName + " success bulk payment, invoices id: " + invoiceIdList);
            invoiceService.setInvoiceBulkAsPaid(invoiceIdList);
        }

        return new ResponseEntity<>(OK);
    }

    @PostMapping(value = "/bankAccount/statement")
    public ResponseEntity<?> getStatement(@RequestBody StatementDateReqDto dateReqDto, BindingResult error) {

        HttpHeaders headers;
        try {
            headers = initHeaders();
        } catch (NoBankAccountException | NoBankConnectionException ex) {
            LOGGER.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), PRECONDITION_FAILED);
        }

        validator.validateStatementDates(dateReqDto, error);
        if (error != null && error.hasErrors()) {
            LOGGER.error(error.getFieldError().getDefaultMessage());
            return new ResponseEntity<>(error.getFieldError().getCode(), PRECONDITION_FAILED);
        }



        HttpEntity<StatementDateReqDto> request = new HttpEntity<>(dateReqDto, headers);
        ResponseEntity<Object> response;
        try {
            response = restTemplate.exchange(bankIP + "/statement/statement", HttpMethod.POST, request, Object.class);
        } catch (Exception ex){
            return new ResponseEntity<>("No connection with bank.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    private HttpHeaders initHeaders() throws NoBankAccountException, NoBankConnectionException {
        HttpHeaders headers = new HttpHeaders();
        try {
            UserBankAccountDto dto = userService.getUserBankAccount();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("countNumber", String.valueOf(dto.getCountNumber()));
            headers.add("accessKey", String.valueOf(dto.getAccessKey()));
        } catch (NullPointerException e) {
            LOGGER.error(e.getMessage());
            throw new NoBankConnectionException();
        }
        return headers;
    }

    private double getBalance() throws NoBankAccountException, NoBankConnectionException {
        HttpEntity request = new HttpEntity<>(initHeaders());
        BalanceDto balanceDto;
        try {
            balanceDto = restTemplate.postForObject(bankIP + "/bankaccount/balance", request, BalanceDto.class);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new NoBankConnectionException();
        }
        return balanceDto.getBalance();
    }

}
