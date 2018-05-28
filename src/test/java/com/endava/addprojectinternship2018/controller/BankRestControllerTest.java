package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.service.UserBankAccountService;
import com.endava.addprojectinternship2018.util.EncryptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class BankRestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private EncryptionUtils encryptionUtilsMock;

    @Autowired
    private RestTemplate restTemplateMock;

    @Autowired
    private UserBankAccountService userBankAccountServiceMock;

    @Test
    public void getBalanceResponseEntityTest() {
        //ResponseEntity<?> responseEntity = bankRestController.getBalanceResponseEntity();

    }
}