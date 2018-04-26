package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.ContractDao;
import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
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

    public List<Contract> getAllContracts(){
        return contractDao.findAll();
    }

    public List<Contract> getContractsByCustomerId(int id) {
        return contractDao.findByCustomerId(id);
    }

    public void createNewContract(ContractDto contractDto) {

        Contract contract = new Contract(contractDto.getIssueDate(),
                contractDto.getExpireDate(), contractDto.getSum());
        contract.setCompany(contractDto.getCompany());
        contract.setCustomer(contractDto.getCustomer());
        contract.setProduct(contractDto.getProduct());

        contractDao.save(contract);

    }

}
