package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.*;
import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.AdvancedFilter;
import com.endava.addprojectinternship2018.model.dto.ContractDto;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static com.endava.addprojectinternship2018.model.enums.ContractStatus.SIGNED_BY_CUSTOMER;
import static com.endava.addprojectinternship2018.model.enums.ContractStatus.UNSIGNED;
import static com.endava.addprojectinternship2018.model.enums.ContractStatus.SIGNED_BY_COMPANY;
import static com.endava.addprojectinternship2018.model.enums.ContractStatus.ACTIVE;

@Service
public class ContractService {

    @Autowired
    private UserUtil userUtil;

    private final ContractDao contractDao;
    private final InvoiceDao invoiceDao;
    private final CompanyDao companyDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    private final WebSocketDistributeService webSocketDistributeService;

    private static final Logger LOGGER = Logger.getLogger(ContractService.class);

    @Autowired
    public ContractService(ContractDao contractDao, InvoiceDao invoiceDao,
                           CompanyDao companyDao, CustomerDao customerDao, ProductDao productDao,
                           WebSocketDistributeService webSocketDistributeService) {
        this.contractDao = contractDao;
        this.invoiceDao = invoiceDao;
        this.customerDao = customerDao;
        this.companyDao = companyDao;
        this.productDao = productDao;
        this.webSocketDistributeService = webSocketDistributeService;
    }

    public List<Contract> getAllByCompanyName(String companyName) {
        return contractDao.findAllByCompanyName(companyName);
    }

    public List<Contract> getAll() {
        return contractDao.findAll();
    }

    public List<Contract> getAllByCompanyId(int companyId) {
        return contractDao.findAllByCompanyIdOrderByIssueDate(companyId);
    }

    public List<Contract> getAllByProductId(int productId) {
        return contractDao.findAllByProductIdOrderByIssueDate(productId);
    }

    public List<Contract> getAllByCustomerId(int id) {
        return contractDao.findAllByCustomerIdOrderByIssueDate(id);
    }

    public Contract getById(int contractId) {
        Optional<Contract> optionalContract = contractDao.findById(contractId);
        if (!optionalContract.isPresent()) {
            LOGGER.error("Contract with id " + contractId + " not found");
            return null;
        }
        return optionalContract.get();
    }

    public Optional<Contract> getByCustomerIdCompanyIdProductId(int customerId, int companyId, int productId) {
        return contractDao.findByCustomerIdAndCompanyIdAndProductId(customerId, companyId, productId);
    }

    public long countByCompanyAndStatus(int companyId, ContractStatus status) {
        return contractDao.countByCompanyIdAndStatus(companyId, status);
    }

    public long countByCustomerAndStatus(int customerId, ContractStatus status) {
        return contractDao.countByCustomerIdAndStatus(customerId, status);
    }

    @Transactional
    public void saveContract(ContractDto contractDto) {
        Contract contract = convertDTOToContract(contractDto);
        contractDao.save(contract);
        if (userUtil.getCurrentUser().getRole() == Role.CUSTOMER) {
            webSocketDistributeService.sendNewContractNotification(contract.getCompany().getUser().getUsername());
        } else {
            webSocketDistributeService.sendNewContractNotification(contract.getCustomer().getUser().getUsername());
        }

        LOGGER.info(String.format("%s: contract saved: %s - %s",
                userUtil.getCurrentUser().getUsername(),
                contract.getCompany().getName(),
                contract.getCustomer().getFullName()
        ));
    }

    @Transactional
    public Set<String> deleteContract(int contractId) {

        Set<String> result = new HashSet<>();
        result.add("OK");

        Contract currentContract = getById(contractId);
        if (currentContract == null) {
            result.add("Contract not found");
            return result;
        }

        Role currentUserRole = userUtil.getCurrentUser().getRole();
        ContractStatus currentContractStatus = currentContract.getStatus();
        if (currentUserRole == Role.CUSTOMER) {
            if (currentContractStatus == SIGNED_BY_COMPANY || currentContractStatus == ACTIVE) {
                result.add("Contract is " + currentContractStatus.toString());
                result.remove("OK");
            }
        } else if (currentUserRole == Role.COMPANY) {
            if (currentContractStatus == SIGNED_BY_CUSTOMER) {
                result.add("Contract is " + currentContractStatus.toString());
                result.remove("OK");
            }
        }
        if (haveInvoices(contractId)) {
            result.add("Contract have active invoices");
            result.remove("OK");
        }
        if (!isNotOverdue(contractId)) {
            result.add("OK");
        }
        if (result.contains("OK")) {
            contractDao.delete(currentContract);
        }

        return result;
    }

    public Contract convertDTOToContract(ContractDto contractDto) {
        Contract contract = new Contract();
        contract.setIssueDate(contractDto.getIssueDate());
        contract.setExpireDate(contractDto.getExpireDate());
        contract.setSum(contractDto.getSum());
        contract.setCompany(companyDao.findById(contractDto.getCompanyId()).get());
        contract.setCustomer(customerDao.findById(contractDto.getCustomerId()).get());
        contract.setProduct(productDao.findById(contractDto.getProductId()).get());
        contract.setStatus(ContractStatus.valueOf(contractDto.getStatus()));
        return contract;
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
                } else {
                    newStatus = UNSIGNED;
                }
                webSocketDistributeService.sendSignContractNotification(currentContract.getCompany().getUser().getUsername(), contractId);
                break;
            case COMPANY:
                if (currentStatus == UNSIGNED) {
                    newStatus = SIGNED_BY_COMPANY;
                } else if (currentStatus == SIGNED_BY_CUSTOMER) {
                    newStatus = ACTIVE;
                } else {
                    newStatus = UNSIGNED;
                }
                webSocketDistributeService.sendSignContractNotification(currentContract.getCustomer().getUser().getUsername(), contractId);
                break;
        }

        if (newStatus != currentStatus) {
            currentContract.setStatus(newStatus);
            contractDao.save(currentContract);
        }

        return newStatus.toString();

    }

    public List<Contract> getAllByCompanyIdFiltered(int currentCompanyId, AdvancedFilter filter) {
        double sumFrom = (filter.getSumFrom() == 0 ? Double.MIN_VALUE : filter.getSumFrom());
        double sumTo = (filter.getSumTo() == 0 ? Double.MAX_VALUE : filter.getSumTo());
        LocalDate dateFrom = (filter.getDateFrom() == null ? LocalDate.of(1, 1, 1) : filter.getDateFrom());
        LocalDate dateTo = (filter.getDateTo() == null ? LocalDate.of(4999, 12, 31) : filter.getDateTo());

        if (filter.getContractStatus() != null) {
            return contractDao.findAllByCompanyIdAndStatusAndSumBetweenAndIssueDateBetweenOrderByIssueDate
                    (currentCompanyId, filter.getContractStatus(), sumFrom, sumTo, dateFrom, dateTo);
        } else {
            return contractDao.findAllByCompanyIdAndSumBetweenAndIssueDateBetweenOrderByIssueDate
                    (currentCompanyId, sumFrom, sumTo, dateFrom, dateTo);
        }
    }

    public List<Contract> getAllByCustomerIdFiltered(int currentCustomerId, AdvancedFilter filter) {
        double sumFrom = (filter.getSumFrom() == 0 ? Double.MIN_VALUE : filter.getSumFrom());
        double sumTo = (filter.getSumTo() == 0 ? Double.MAX_VALUE : filter.getSumTo());
        LocalDate dateFrom = (filter.getDateFrom() == null ? LocalDate.of(1, 1, 1) : filter.getDateFrom());
        LocalDate dateTo = (filter.getDateTo() == null ? LocalDate.of(4999, 12, 31) : filter.getDateTo());

        if (filter.getContractStatus() != null) {
            return contractDao.findAllByCustomerIdAndStatusAndSumBetweenAndIssueDateBetweenOrderByIssueDate
                    (currentCustomerId, filter.getContractStatus(), sumFrom, sumTo, dateFrom, dateTo);
        } else {
            return contractDao.findAllByCustomerIdAndSumBetweenAndIssueDateBetweenOrderByIssueDate
                    (currentCustomerId, sumFrom, sumTo, dateFrom, dateTo);
        }
    }

    public Contract getLastContract() {
        return contractDao.findFirstByOrderByIdDesc().get(0);
    }

    public boolean isNotOverdue(int contractId) {
        Contract currentContract = getById(contractId);
        return currentContract != null && currentContract.getExpireDate().isAfter(LocalDate.now());
    }

    public boolean haveInvoices(int contractId) {
        return invoiceDao.countAllByContractId(contractId) > 0;
    }
}
