package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.InvoiceDao;
import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.dto.AdvancedFilter;
import com.endava.addprojectinternship2018.model.dto.PaymentDto;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.dto.InvoiceDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.endava.addprojectinternship2018.model.enums.InvoiceStatus.*;

@Service
public class InvoiceService {

    private final InvoiceDao invoiceDao;
    private final WebSocketDistributeService webSocketDistributeService;
    private final ContractService contractService;

    private static final Logger LOGGER = Logger.getLogger(InvoiceService.class);

    @Autowired
    public InvoiceService(InvoiceDao invoiceDao, WebSocketDistributeService webSocketDistributeService,
                          ContractService contractService) {
        this.invoiceDao = invoiceDao;
        this.webSocketDistributeService = webSocketDistributeService;
        this.contractService = contractService;
    }

    public Invoice getInvoiceById(int invoiceId) {
        return invoiceDao.findById(invoiceId).get();
    }

    public List<Invoice> getCheckedInvoices(List<Integer> contractIds) {

        List<Invoice> invoiceList = new ArrayList<>();

        for (Integer id : contractIds) {
            boolean success = true;
            Contract contract = contractService.getById(id);
            for (Invoice inv : invoiceDao.findAllByContractId(id)) {
                if (LocalDate.now().isBefore(inv.getServiceEndDate())) {
                    success = false;
                }
            }

            if (success) {
                Invoice temp = new Invoice();
                LocalDate currentDate = LocalDate.now();
                temp.setSum(contract.getSum());
                temp.setIssueDate(currentDate);
                temp.setServiceStartDate(currentDate.withDayOfMonth(1));
                temp.setServiceEndDate(currentDate.withDayOfMonth(currentDate.lengthOfMonth()));
                temp.setDueDate(currentDate.withDayOfMonth(currentDate.lengthOfMonth()).plusDays(15));
                temp.setStatus(InvoiceStatus.ISSUED);
                temp.setContract(contract);
                invoiceList.add(temp);
            }
        }
        return invoiceList;
    }

    public List<Invoice> getAllByCustomerId(int customerId) {
        return invoiceDao.findAllByContractCustomerIdAndStatusIsInOrderByIssueDate(customerId, getStatusesForCustomer());
    }

    public List<Invoice> getInvoicesByCompanyId(int id) {
        return invoiceDao.findAllByContract_Company_IdOrderById(id);
    }

    @Transactional
    public void save(Invoice invoice) {
        invoiceDao.save(invoice);
    }

    @Transactional
    public void save(Iterable<Invoice> invoices) {
        invoiceDao.saveAll(invoices);
    }

    @Transactional
    public void saveInvoiceDto(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice(invoiceDto.getSum(), invoiceDto.getIssueDate()
                , invoiceDto.getDueDate(), invoiceDto.getStatus(), invoiceDto.getContract());
        invoiceDao.save(invoice);
    }

    @Transactional
    public void deleteInvoice(int id) {
        invoiceDao.deleteById(id);
    }

    @Transactional
    public void changeInvoiceStatusToSent(int invoiceId) {
        Invoice invoice = invoiceDao.findById(invoiceId).get();
        System.out.println(invoice);
        LOGGER.info("current invoice status:" + invoice.getStatus());
        if (invoice.getStatus() == InvoiceStatus.ISSUED) {
            invoice.setStatus(SENT);
            invoiceDao.save(invoice);
            webSocketDistributeService.sendNewInvoiceNotification(invoice.getContract().getCustomer().getUser().getUsername(),
                    invoiceId, invoice.getContract().getCompany().getName());
        } else System.out.println("Status not corresponding to ISSUED !!!");
    }

    @Transactional
    public void setInvoiceAsPaid(int id) {
        Invoice invoice = invoiceDao.findById(id).get();
        invoice.setStatus(PAID);
        invoiceDao.save(invoice);
        webSocketDistributeService.sendNewInvoicePaidNotification(invoice.getContract().getCompany().getUser().getUsername(),
                id, invoice.getContract().getCustomer().getFullName());
    }

    public long countByCustomerIdAndStatus(int customerId, InvoiceStatus status) {
        return invoiceDao.countAllByContractCustomerIdAndStatus(customerId, status);
    }

    public long countByCompanyIdAndStatus(int companyId, InvoiceStatus status) {
        return invoiceDao.countAllByContractCompanyIdAndStatus(companyId, status);
    }

    public List<Invoice> getInvoicesByCustomerIdFiltered(int customerId, AdvancedFilter filter) {
        double sumFrom = (filter.getSumFrom() == 0 ? Double.MIN_VALUE : filter.getSumFrom());
        double sumTo = (filter.getSumTo() == 0 ? Double.MAX_VALUE : filter.getSumTo());
        LocalDate dateFrom = (filter.getDateFrom() == null ? LocalDate.of(1, 1, 1) : filter.getDateFrom());
        LocalDate dateTo = (filter.getDateTo() == null ? LocalDate.of(4999, 12, 31) : filter.getDateTo());

        if (filter.getInvoiceStatus() == null) {
            return invoiceDao.findAllByContractCustomerIdAndStatusInAndSumBetweenAndIssueDateBetweenOrderByIssueDate
                    (customerId, getStatusesForCustomer(), sumFrom, sumTo, dateFrom, dateTo);
        }
        return invoiceDao.findAllByContractCustomerIdAndStatusAndSumBetweenAndIssueDateBetweenOrderByIssueDate
                (customerId, filter.getInvoiceStatus(), sumFrom, sumTo, dateFrom, dateTo);
    }

    public List<Invoice> getInvoicesByCompanyIdFiltered(int companyId, AdvancedFilter filter) {
        double sumFrom = (filter.getSumFrom() == 0 ? Double.MIN_VALUE : filter.getSumFrom());
        double sumTo = (filter.getSumTo() == 0 ? Double.MAX_VALUE : filter.getSumTo());
        LocalDate dateFrom = (filter.getDateFrom() == null ? LocalDate.of(1, 1, 1) : filter.getDateFrom());
        LocalDate dateTo = (filter.getDateTo() == null ? LocalDate.of(4999, 12, 31) : filter.getDateTo());

        if (filter.getInvoiceStatus() == null) {
            return invoiceDao.findAllByContractCompanyIdAndSumBetweenAndIssueDateBetweenOrderByIssueDate
                    (companyId, sumFrom, sumTo, dateFrom, dateTo);
        }
        return invoiceDao.findAllByContractCompanyIdAndStatusAndSumBetweenAndIssueDateBetweenOrderByIssueDate
                (companyId, filter.getInvoiceStatus(), sumFrom, sumTo, dateFrom, dateTo);
    }

    public List<InvoiceStatus> getStatusesForCustomer() {
        return Arrays.stream(InvoiceStatus.values())
                .filter(invoiceStatus -> invoiceStatus == PAID || invoiceStatus == SENT || invoiceStatus == OVERDUE)
                .collect(Collectors.toList());
    }

    public List<Invoice> getInvoiceInPeriodService(int contract_id, LocalDate issueDate) {
        return invoiceDao.findInvoiceInPeriod(contract_id, issueDate);
    }

    public void setInvoiceBulkAsPaid(List<Integer> idList) {
        for (Integer i : idList) {
            setInvoiceAsPaid(i);
        }
    }

    public PaymentDto createPaymentDto(int invoiceId) {
        Invoice currentInvoice = getInvoiceById(invoiceId);
        Contract currentContract = currentInvoice.getContract();
        Long companyCount = currentContract.getCompany().getCountNumber();
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setC(companyCount);
        paymentDto.setS(currentInvoice.getSum());
        if (companyCount == null) {
            paymentDto.setD("Company " + currentContract.getCompany().getName() + " has not bank account!");
        } else {
            paymentDto.setD("invoiceId=" + invoiceId);
        }
        return paymentDto;
    }

}
