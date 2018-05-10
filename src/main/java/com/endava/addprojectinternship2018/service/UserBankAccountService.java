package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.dto.UserBankAccountDto;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBankAccountService {

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CustomerService customerService;

    public void save(UserBankAccountDto userBankAccountDto){
        Company company = userUtil.getCurrentCompany();

        if (company != null){
            if (company.getCountNumber() == null && company.getAccessKey() == null)
                company.setCountNumber(userBankAccountDto.getCountNumber());
                company.setAccessKey(userBankAccountDto.getAccessKey());
                companyService.save(company);
        } else {
            Customer customer = userUtil.getCurrentCustomer();
            if (customer.getCountNumber() == null && customer.getAccessKey() == null){
                customer.setCountNumber(userBankAccountDto.getCountNumber());
                customer.setAccessKey(userBankAccountDto.getAccessKey());
                customerService.save(customer);
            }
        }
    }
}
