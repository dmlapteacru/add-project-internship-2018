package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.BankKeyDao;
import com.endava.addprojectinternship2018.model.BankKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankKeyService {

    private final BankKeyDao bankKeyDao;

    @Autowired
    public BankKeyService(BankKeyDao bankKeyDao) {
        this.bankKeyDao = bankKeyDao;
    }

    public void saveBankKey(BankKey bankKey) {
        bankKeyDao.save(bankKey);
    }

}
