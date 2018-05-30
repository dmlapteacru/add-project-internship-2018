package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.exception.NoBankAccountException;
import com.endava.addprojectinternship2018.exception.NoBankConnectionException;
import com.endava.addprojectinternship2018.model.dto.*;
import com.endava.addprojectinternship2018.service.InvoiceService;
import com.endava.addprojectinternship2018.service.UserBankAccountService;
import com.endava.addprojectinternship2018.service.UserService;
import com.endava.addprojectinternship2018.util.UserUtil;
import com.endava.addprojectinternship2018.validation.Validator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@RestController
public class BankRestController {

    private UserUtil userUtil;
    private final String bankIP;
    private final RestTemplate restTemplate;
    private final UserBankAccountService userBankAccountService;
    private final UserService userService;
    private final InvoiceService invoiceService;
    private final Validator validator;

    private static final Logger LOGGER = Logger.getLogger(com.endava.addprojectinternship2018.controller.BankRestController.class);

    @Autowired
    public BankRestController(@Qualifier("bank_ip") String bankIP, RestTemplate restTemplate,
                              UserBankAccountService userBankAccountService, UserService userService,
                              InvoiceService invoiceService, Validator validator) {
        this.bankIP = bankIP;
        this.restTemplate = restTemplate;
        this.userBankAccountService = userBankAccountService;
        this.userService = userService;
        this.invoiceService = invoiceService;
        this.validator = validator;
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
        try {
            UserBankAccountDto dto = restTemplate.postForObject(bankIP + "/bankaccount/create", request, UserBankAccountDto.class);
            userBankAccountService.save(dto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return new ResponseEntity<>(BAD_REQUEST);
        }

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
            return new ResponseEntity<>("Sum must be greater than 0.01 MDL.", PRECONDITION_FAILED);
        }
        HttpHeaders headers;
        try {
            headers = initHeaders();
        } catch (NoBankAccountException | NoBankConnectionException ex) {
            LOGGER.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), PRECONDITION_FAILED);
        }
        Map<String, Double> body = new HashMap<>();
        body.put("sum", sum);
        try {
            HttpEntity<Map> request = new HttpEntity<>(body, headers);
            restTemplate.postForObject(bankIP + "/bankaccount/addmoney", request, String.class);
        } catch (Exception ex) {
            LOGGER.error("add money error: " + ex.getMessage());
            return new ResponseEntity<>("No connection with bank", PRECONDITION_FAILED);
        }

        LOGGER.info(userUtil.getCurrentUser().getUsername() + " added money successful");
        return new ResponseEntity<>("Money was added", OK);
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

        List<TransactionRequestDto> allTransactions = new LinkedList<>();
        double balanceBefore = 0;
        try {
            HttpEntity<StatementDateReqDto> request = new HttpEntity<>(dateReqDto, headers);
            StatementRequestDto statementRequestDto = restTemplate.postForObject(bankIP + "/statement/statement", request, StatementRequestDto.class);
            balanceBefore = statementRequestDto.getBalanceBefore();
            allTransactions.addAll(statementRequestDto.getListOfTransactions());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return new ResponseEntity<>("No connection with bank.", BAD_REQUEST);
        }

        StatementDto statementDto = userBankAccountService.createStatementDto(balanceBefore, allTransactions);

        return new ResponseEntity<>(statementDto, OK);
    }

    private HttpHeaders initHeaders() throws NoBankAccountException, NoBankConnectionException {
        HttpHeaders headers = new HttpHeaders();
        try {
            UserBankAccountDto dto = userService.getUserBankAccount();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("countNumber", String.valueOf(dto.getCountNumber()));
            headers.add("accessKey", String.valueOf(dto.getAccessKey()));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NoBankConnectionException();
        }
        return headers;
    }

    private double getBalance() throws NoBankAccountException, NoBankConnectionException {
        HttpEntity request = new HttpEntity<>("param", initHeaders());
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
