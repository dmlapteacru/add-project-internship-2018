package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.*;
import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.model.dto.ContractDtoTest;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.endava.addprojectinternship2018.model.enums.ContractStatus.SIGNED_BY_CUSTOMER;
import static com.endava.addprojectinternship2018.model.enums.ContractStatus.UNSIGNED;
import static com.endava.addprojectinternship2018.model.enums.ContractStatus.SIGNED_BY_COMPANY;
import static com.endava.addprojectinternship2018.model.enums.ContractStatus.ACTIVE;

@Service
public class ContractService {

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private ProductDao productDao;

    public List<Contract> getAllByCompanyName(String companyName) {
        return contractDao.findAllByCompanyName(companyName);
    }

    public List<Contract> getAll() {
        return contractDao.findAll();
    }

    public List<Contract> getAllByCompanyId(int companyId) {
        return contractDao.findAllByCompanyId(companyId);
    }

    public List<Contract> getAllByProductId(int productId) {
        return contractDao.findAllByProductId(productId);
    }

    public List<Contract> getAllByCustomerId(int id) {
        return contractDao.findAllByCustomerId(id);
    }

    public Contract getById(int contractId) {
        return contractDao.findById(contractId).get();
    }

    @Transactional
    public void saveContract(ContractDto contractDto) {
        contractDao.save(convertContractDtoToContract(contractDto));
    }

    @Transactional
    public String deleteContract(int contractId) {
        Optional<Contract> contractOptional = contractDao.findById(contractId);
        User currentUser = userUtil.getCurrentUser();
        if (contractOptional.isPresent()) {
            if (currentUser.getRole() == Role.CUSTOMER && contractOptional.get().getStatus() != SIGNED_BY_CUSTOMER) {
                return contractOptional.get().getStatus() + " contract can not be deleted";
            }
            contractDao.delete(contractOptional.get());
            return "OK";
        }
        return "Contract not found";
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

    public ContractDto createUpdateContractDto(int contractId) {
        ContractDto contractDto = convertContractToContractDto(contractDao.findById(contractId).get());
        List<Customer> customerList = new ArrayList<>();
        customerList.add(contractDto.getSelectedCustomer());
        contractDto.setCustomers(customerList);
        List<Company> companyList = new ArrayList<>();
        companyList.add(contractDto.getSelectedCompany());
        contractDto.setCompanies(companyList);
        List<Product> productList = new ArrayList<>();
        productList.add(contractDto.getSelectedProduct());
        contractDto.setProducts(productList);
        return contractDto;
    }

    public ContractDto createNewContractDto(int customerId, int companyId, int productId) {

        ContractDto contractDto = new ContractDto();

        List<Customer> customerList = new ArrayList<>();
        List<Company> companyList = new ArrayList<>();
        List<Product> productList = new ArrayList<>();
        if (customerId != 0) {
            customerDao.findById(customerId).ifPresent(customer -> {
                customerList.add(customer);
                contractDto.setSelectedCustomer(customer);
            });
        }
        if (customerList.isEmpty()) {
            customerList.addAll(customerDao.findAll());
        }
        if (companyId != 0) {
            companyDao.findById(companyId).ifPresent(company -> {
                companyList.add(company);
                contractDto.setSelectedCompany(company);
            });
        }
        if (companyList.isEmpty()) {
            companyList.addAll(companyDao.findAll());
        }
        if (productId != 0) {
            productDao.findById(productId).ifPresent(product -> {
                productList.add(product);
                contractDto.setSelectedProduct(product);
                contractDto.setSum(product.getPrice());
            });
        }
        if (productList.isEmpty()) {
            productList.addAll(productDao.findAllByCompanyId(companyId));
        }
        if (productList.isEmpty()) {
            productList.addAll(productDao.findAll());
        }

        contractDto.setProducts(productList);
        contractDto.setCompanies(companyList);
        contractDto.setCustomers(customerList);
        contractDto.setIssueDate(LocalDate.now());
        contractDto.setStatus(ContractStatus.UNSIGNED);

        return contractDto;
    }

    @Transactional
    public String signContract(int contractId) {

        Contract currentContract = contractDao.findById(contractId).get();
        ContractStatus currentStatus = currentContract.getStatus();
        ContractStatus newStatus = currentStatus;
        User currentUser = userUtil.getCurrentUser();

        switch (currentUser.getRole()) {
            case CUSTOMER:
                if (currentStatus == UNSIGNED) {
                    newStatus = SIGNED_BY_CUSTOMER;
                } else if (currentStatus == SIGNED_BY_COMPANY) {
                    newStatus = ACTIVE;
                }
                break;
            case COMPANY:
                if (currentStatus == UNSIGNED) {
                    newStatus = SIGNED_BY_COMPANY;
                } else if (currentStatus == SIGNED_BY_CUSTOMER) {
                    newStatus = ACTIVE;
                }
                break;
        }

        if (newStatus != currentStatus) {
            currentContract.setStatus(newStatus);
            contractDao.save(currentContract);
        }

        return newStatus.toString();

    }

}
