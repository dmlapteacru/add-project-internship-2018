package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.dao.CustomerDao;
import com.endava.addprojectinternship2018.dao.InvoiceDao;
import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.StatementDto;
import com.endava.addprojectinternship2018.model.dto.TransactionDto;
import com.endava.addprojectinternship2018.model.dto.TransactionRequestDto;
import com.endava.addprojectinternship2018.model.dto.UserBankAccountDto;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserBankAccountService {

    @Autowired
    private UserUtil userUtil;

    private final CompanyDao companyDao;
    private final CustomerDao customerDao;
    private final InvoiceDao invoiceDao;

    @Autowired
    public UserBankAccountService(CompanyDao companyDao, CustomerDao customerDao, InvoiceDao invoiceDao) {
        this.companyDao = companyDao;
        this.customerDao = customerDao;
        this.invoiceDao = invoiceDao;
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
                companyDao.save(currentCompany);
            }
        } else {
            Customer currentCustomer = userUtil.getCurrentCustomer();
            if (currentCustomer.getCountNumber() == null && currentCustomer.getBankKey() == null) {
                currentCustomer.setCountNumber(userBankAccountDto.getCountNumber());
                currentCustomer.setBankKey(bankKey);
                customerDao.save(currentCustomer);
            }
        }
    }

    public StatementDto createStatementDto(double balanceBefore, List<TransactionRequestDto> transactionRequestDtoList) {

        boolean isCompany = (userUtil.getCurrentUser().getRole() == Role.COMPANY);

        List<TransactionDto> transactionDtoList = new LinkedList<>();
        double balanceAfter = balanceBefore;
        for (TransactionRequestDto dto : transactionRequestDtoList) {
            TransactionDto transactionDto = new TransactionDto();
            if (isCompany) {
                Optional<Customer> optionalCustomer = customerDao.findByCountNumber(dto.getC());
                if (optionalCustomer.isPresent()) {
                    transactionDto.setPartnerName(optionalCustomer.get().getFullName());
                } else {
                    transactionDto.setPartnerName(companyDao.findByCountNumber(dto.getM()).get().getName());
                }
            } else {
                Optional<Company> optionalCompany = companyDao.findByCountNumber(dto.getC());
                if (optionalCompany.isPresent()) {
                    transactionDto.setPartnerName(optionalCompany.get().getName());
                } else {
                    transactionDto.setPartnerName(customerDao.findByCountNumber(dto.getM()).get().getFullName());
                }
            }
            String fullDescription;
            try {
                String[] str = dto.getDr().split("=");
                int invoiceId = Integer.valueOf(str[1]);
                Invoice currentInvoice = invoiceDao.findById(invoiceId).get();
                StringBuilder sb = new StringBuilder();
                sb.append("Invoice period: ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                sb.append(currentInvoice.getServiceStartDate().format(formatter));
                sb.append("-");
                sb.append(currentInvoice.getServiceEndDate().format(formatter));
                sb.append(". Service: ");
                sb.append(currentInvoice.getContract().getProduct().getName());
                fullDescription = sb.toString();
            } catch (Exception ex) {
                fullDescription = dto.getDr();
            }
            balanceAfter += dto.getS();
            transactionDto.setDate(dto.getD());
            transactionDto.setDescription(fullDescription);
            transactionDto.setSum(dto.getS());
            transactionDto.setCurrentBalance(balanceAfter);
            transactionDtoList.add(transactionDto);
        }

        StatementDto result = new StatementDto();
        result.setStartBalance(balanceBefore);
        result.setEndBalance(balanceAfter);
        result.setListOfTransactions(transactionDtoList);

        return result;
    }

}
