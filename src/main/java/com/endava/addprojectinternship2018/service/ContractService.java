package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.ContractDao;
import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContractService {

    @Autowired
    private ContractDao contractDao;

    public List<Contract> getContractsByCompanyName(String companyName) {
        return contractDao.findByCompanyName(companyName);
    }

    public List<Contract> getAllContracts() {
        return contractDao.findAll();
    }

    public List<Contract> getContractsByCustomerId(int id) {
        return contractDao.findByCustomerId(id);
    }

    public void saveContract(ContractDto contractDto) {
        contractDao.save(convertContractDtoToContract(contractDto));
    }

    public Contract getContractById(int contractId) {
        return contractDao.findById(contractId).get();
    }

    public ContractDto convertContractToContractDto(Contract contract) {
        ContractDto contractDto = new ContractDto();
        contractDto.setSelectedCompany(contract.getCompany());
        contractDto.setSelectedCustomer(contract.getCustomer());
        contractDto.setSelectedProduct(contract.getProduct());
        contractDto.setIssueDate(contract.getIssueDate());
        contractDto.setStatus(contract.getStatus());
        contractDto.setExpireDate(contract.getExpireDate());
        contractDto.setSum(contract.getSum());
        contractDto.setContractId(contract.getId());
        return contractDto;
    }

    public Contract convertContractDtoToContract(ContractDto contractDto) {
        Contract contract = contractDao.findById(contractDto.getContractId())
                .orElseGet(Contract::new);
        contract.setIssueDate(contractDto.getIssueDate());
        contract.setExpireDate(contractDto.getExpireDate());
        contract.setSum(contractDto.getSum());
        contract.setCompany(contractDto.getSelectedCompany());
        contract.setCustomer(contractDto.getSelectedCustomer());
        contract.setProduct(contractDto.getSelectedProduct());
        contract.setStatus(contractDto.getStatus());
        return contract;
    }
}
