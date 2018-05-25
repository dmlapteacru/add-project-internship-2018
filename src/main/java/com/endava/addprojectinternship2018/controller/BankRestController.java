package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.exception.NoBankAccountException;
import com.endava.addprojectinternship2018.model.BankKey;
import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.dto.PaymentDto;
import com.endava.addprojectinternship2018.model.dto.StatementDateReqDto;
import com.endava.addprojectinternship2018.model.dto.StatementDto;
import com.endava.addprojectinternship2018.model.dto.UserBankAccountDto;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.InvoiceService;
import com.endava.addprojectinternship2018.service.UserBankAccountService;
import com.endava.addprojectinternship2018.service.UserService;
import com.endava.addprojectinternship2018.util.EncryptionUtils;
import com.endava.addprojectinternship2018.util.UserUtil;
import com.endava.addprojectinternship2018.validation.CustomUsernameValidator;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.ConnectException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
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

    @Autowired
    private EncryptionUtils encryptionUtils;

    private static final Logger LOGGER = Logger.getLogger(com.endava.addprojectinternship2018.controller.BankRestController.class);

    @RequestMapping(value = "/bankAccount/create", method = POST)
    public ResponseEntity<UserBankAccountDto> newAccount() {

        KeyPair keyPair = encryptionUtils.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String modulus = encryptionUtils.getModulus(publicKey);
        try {
            encryptionUtils.init(null, keyPair.getPrivate().getEncoded());
        } catch (InvalidKeySpecException e) {
            LOGGER.error(e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity request = new HttpEntity<>(modulus, headers);
        ResponseEntity<Object> response = restTemplate.postForEntity(bankIP + "/bankaccount/create", request, Object.class);
        try {
            UserBankAccountDto userBankAccountDto = (UserBankAccountDto) encryptionUtils.decryptData(response.getBody().toString(), UserBankAccountDto.class);
            userBankAccountService.save(userBankAccountDto, keyPair);
        } catch (BadPaddingException | IllegalBlockSizeException | IOException | InvalidKeyException e) {
            LOGGER.error(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/balance", method = POST)
    public ResponseEntity<?> getBalance() throws ConnectException {

        HttpEntity request = new HttpEntity<>(initConnection());
        String balance;
        try {
            ResponseEntity response = restTemplate.postForEntity(bankIP + "/bankaccount/balance", request, Object.class);
            balance = encryptionUtils.decryptData(response.getBody().toString(), Object.class).toString();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return new ResponseEntity<>("No connection", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/addmoney", method = POST)
    public ResponseEntity<?> addMoney(@RequestParam Double sum) {

        if (sum == null || sum < 0.01) {
            return new ResponseEntity<>("Must be greater than 0.01 MDL.", HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = initConnection();
        Map<String, Double> body = new HashMap<>();
        body.put("sum", sum);
        try {
            String encryptedData = encryptionUtils.encryptData(body);
            HttpEntity<String> request = new HttpEntity<>(encryptedData, headers);
            restTemplate.postForObject(bankIP + "/bankaccount/addmoney", request, String.class);
        } catch (Exception ex) {
            return new ResponseEntity<>("No connection to bank", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Money added.", HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/payinvoice", method = POST)
    public ResponseEntity<?> payInvoice(@RequestParam(name = "bal") Double balance, @RequestBody PaymentDto paymentDto, BindingResult error) {

        HttpHeaders headers = initConnection();

        int idInvoice = paymentDto.getC().intValue();
        paymentDto.setD(paymentDto.getD() + invoiceService.setInvoiceDescription(paymentDto.getC().intValue()).getFullDescription());
        paymentDto.setC(companyService.getCompanyByInvoiceId(paymentDto.getC().intValue()).getCountNumber());

        validator.validateInvoicePayment(balance, paymentDto, error);
        if (error.hasErrors()) {
            LOGGER.error(error.getFieldError().getDefaultMessage());
            return new ResponseEntity<>(error.getFieldError().getCode(), HttpStatus.NOT_FOUND);
        }

        ResponseEntity<Object> response;
        try {
            String encrypted = encryptionUtils.encryptData(paymentDto);
            HttpEntity<String> request = new HttpEntity<>(encrypted, headers);
            response = restTemplate.postForEntity(bankIP + "/sendmoney/sendmoney", request, Object.class);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return new ResponseEntity<>("No connection with bank", HttpStatus.BAD_REQUEST);
        }

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            invoiceService.setInvoiceAsPaid(idInvoice);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/payinvoice/bulk", method = POST)
    public ResponseEntity<?> payInvoice(@RequestParam(name = "bal") Double balance, @RequestBody List<PaymentDto> paymentDtoList, BindingResult error) {

        HttpHeaders headers = initConnection();

        List<Integer> idList = new ArrayList<>();
        for (PaymentDto p : paymentDtoList) {
            idList.add(p.getC().intValue());
            p.setD(p.getD() +
                    invoiceService.setInvoiceDescription(p.getC().intValue())
                            .getFullDescription());
            p.setC(companyService.getCompanyByInvoiceId(p.getC().intValue())
                    .getCountNumber());
        }

        validator.validateBulkInvoicePayment(balance, paymentDtoList, error);
        if (error.hasErrors()) {
            LOGGER.error(error.getFieldError().getDefaultMessage());
            return new ResponseEntity<>("Not enough money.", HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<Object> response;
        try {
            String encrypted = encryptionUtils.encryptData(paymentDtoList);
            HttpEntity<String> request = new HttpEntity<>(encrypted, headers);
            response = restTemplate.postForEntity(bankIP + "/sendmoney/bulkpayment", request, Object.class);
        } catch (Exception ex) {
            return new ResponseEntity<>("No connection with bank.", HttpStatus.BAD_REQUEST);
        }

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            invoiceService.setInvoiceBulkAsPaid(idList);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/bankAccount/statement", method = POST)
    public ResponseEntity<?> getStatement(@RequestBody StatementDateReqDto dateReqDto, BindingResult error) {

        validator.validateStatementDates(dateReqDto, error);
        if (error.hasErrors()) {
            LOGGER.error(error.getFieldError().getDefaultMessage());
            return new ResponseEntity<>(error.getFieldError().getCode(), HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = initConnection();

        ResponseEntity<Object> response;
        try {
            String encrypted = encryptionUtils.encryptData(dateReqDto);
            HttpEntity<String> request = new HttpEntity<>(encrypted, headers);
            response = restTemplate.exchange(bankIP + "/statement/statement", HttpMethod.POST, request, Object.class);
        } catch (Exception ex) {
            return new ResponseEntity<>("No connection with bank.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
    }

    private HttpHeaders initConnection() {
        HttpHeaders headers = new HttpHeaders();
        try {
            UserBankAccountDto dto = userService.getUserBankAccount();
            encryptionUtils.init(dto.getModulus(), dto.getPrivateKey());
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            headers.add("countNumber", String.valueOf(dto.getCountNumber()));
        } catch (InvalidKeySpecException
                | NoBankAccountException
                | NullPointerException e) {
            LOGGER.error(e.getMessage());
        }
        return headers;
    }

}
