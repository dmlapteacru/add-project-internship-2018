package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.model.BankKey;
import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.dto.UserBankAccountDto;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;

@Service
public class UserBankAccountService {

    @Autowired
    private UserUtil userUtil;

    private final CompanyService companyService;
    private final CustomerService customerService;
    private final BankKeyService bankKeyService;

    @Autowired
    public UserBankAccountService(CompanyService companyService, CustomerService customerService, BankKeyService bankKeyService) {
        this.companyService = companyService;
        this.customerService = customerService;
        this.bankKeyService = bankKeyService;
    }

    @Transactional
    public void save(UserBankAccountDto userBankAccountDto, KeyPair keyPair) {

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        byte[] bankModulus = Base64.getDecoder().decode(userBankAccountDto.getBankPublicKeyModulus());
        BankKey bankKey = new BankKey(bankModulus, privateKey.getEncoded());

        if (userUtil.getCurrentUser().getRole() == Role.COMPANY) {
            Company currentCompany = userUtil.getCurrentCompany();
            if (currentCompany.getCountNumber() == null && currentCompany.getBankKey() == null) {
                currentCompany.setCountNumber(userBankAccountDto.getCountNumber());
                currentCompany.setBankKey(bankKey);
                companyService.save(currentCompany);
            }
        } else {
            Customer currentCustomer = userUtil.getCurrentCustomer();
            if (currentCustomer.getCountNumber() == null && currentCustomer.getBankKey() == null) {
                currentCustomer.setCountNumber(userBankAccountDto.getCountNumber());
                currentCustomer.setBankKey(bankKey);
                customerService.save(currentCustomer);
            }
        }
    }

}
