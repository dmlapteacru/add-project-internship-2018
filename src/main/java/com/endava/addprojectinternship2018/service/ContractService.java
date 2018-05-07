package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.ContractDao;
import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.omg.PortableInterceptor.ACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContractService {

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private UserUtil userUtil;

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

    public String deleteContract(int contractId) {
        Optional<Contract> contractOptional = contractDao.findById(contractId);
        User currentUser = userUtil.getCurrentUser();
        if (contractOptional.isPresent()) {
            if (currentUser.getRole() == Role.CUSTOMER && contractOptional.get().getStatus() != ContractStatus.SIGNED_BY_CUSTOMER) {
                return contractOptional.get().getStatus() + " contract can not be deleted";
            }
            contractDao.delete(contractOptional.get());
            return "OK";
        }
        return "Contract not found";
    }
}
