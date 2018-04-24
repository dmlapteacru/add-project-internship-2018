package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.ContractDao;
import com.endava.addprojectinternship2018.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {

    @Autowired
    private ContractDao contractDao;

    public List<Contract> getContractsByCompanyName(String companyName){
        return contractDao.findByCompanyName(companyName);
    }

}
