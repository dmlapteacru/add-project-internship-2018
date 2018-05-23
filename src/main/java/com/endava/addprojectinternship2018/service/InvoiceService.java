package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.InvoiceDao;
import com.endava.addprojectinternship2018.dao.InvoiceTransactionDao;
import com.endava.addprojectinternship2018.model.dto.AdvancedFilter;
import com.endava.addprojectinternship2018.model.dto.InvoiceDescriptionPaymentDto;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.dto.InvoiceDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.endava.addprojectinternship2018.model.enums.InvoiceStatus.*;

@Service
public class InvoiceService {

    private final InvoiceDao invoiceDao;
    private final InvoiceTransactionDao invoiceTransactionDao;
    private final WebSocketDistributeService webSocketDistributeService;

    private static final Logger LOGGER = Logger.getLogger(InvoiceService.class);

    @Autowired
    public InvoiceService(InvoiceDao invoiceDao, InvoiceTransactionDao invoiceTransactionDao, WebSocketDistributeService webSocketDistributeService) {
        this.invoiceDao = invoiceDao;
        this.invoiceTransactionDao = invoiceTransactionDao;
        this.webSocketDistributeService = webSocketDistributeService;
    }

    public List<Invoice> getAllInvoices(){
        return invoiceDao.findAll();
    }

    public Invoice getInvoiceById(int invoiceId) {
        return invoiceDao.findById(invoiceId).get();
    }

    public List<Invoice> getAllInvoiceByContractId(int id) {
        return invoiceDao.findByContractIdOrderByIssueDate(id);
    }

    public List<Invoice> getAllInvoicesByStatus(InvoiceStatus status) {
        return invoiceDao.findAllByStatusOrderByIssueDate(status);
    }

    public List<Invoice> getAllInvoicesByIssueDate(LocalDate localDate) {
        return invoiceDao.findAllByIssueDate(localDate);
    }

    public List<Invoice> getAllInvoicesByDueDate(LocalDate localDate) {
        return invoiceDao.findAllByDueDate(localDate);
    }

    public List<Invoice> getAllByCustomerId(int customerId) {
        return invoiceDao.findAllByContractCustomerIdAndStatusIsInOrderByIssueDate(customerId, getStatusesForCustomer());
    }

    public List<Invoice> getInvoicesByCompanyId(int id) {
        return invoiceDao.findAllByContract_Company_IdOrderById(id);
    }

    public List<Invoice> getInvoicesByCompany(String name) {
        return invoiceDao.findAllByContract_Company_Name(name);
    }

    public List<Invoice> getInvoicesByPeriodAndCompanyAndStatus(String date, int contract_id, String status){
        return invoiceTransactionDao.getInvoiceByPeriodContractStatus(date, contract_id, status);
    }

    @Transactional
    public void save(Invoice invoice) {
        invoiceDao.save(invoice);
    }

    @Transactional
    public void saveInvoiceDto(InvoiceDto invoiceDto){
        Invoice invoice = new Invoice(invoiceDto.getSum(), invoiceDto.getIssueDate()
                , invoiceDto.getDueDate(), invoiceDto.getStatus(), invoiceDto.getContract());
        invoiceDao.save(invoice);
    }

    @Transactional
    public void deleteInvoice(int id) {
        invoiceDao.deleteById(id);
    }

    @Deprecated
    public Invoice convertInvoiceDtoToInvoice(InvoiceDto invoiceDto) {
        Invoice invoice = invoiceDao.findById(invoiceDto.getInvoiceId())
                .orElseGet(Invoice::new);
        invoice.setContract(invoiceDto.getContract());
        invoice.setIssueDate(invoiceDto.getIssueDate());
        invoice.setDueDate(invoiceDto.getDueDate());
        invoice.setSum(invoiceDto.getSum());
        invoice.setStatus(invoiceDto.getStatus());
        return invoice;
    }

    @Deprecated
    public InvoiceDto convertInvoiceToInvoiceDto(Invoice invoice) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setContract(invoice.getContract());
        invoiceDto.setDueDate(invoice.getDueDate());
        invoiceDto.setIssueDate(invoice.getIssueDate());
        invoiceDto.setStatus(invoice.getStatus());
        invoiceDto.setSum(invoice.getSum());
        return invoiceDto;
    }

    @Transactional
    public void changeInvoiceStatus(int invoiceId) {
        Invoice invoice = invoiceDao.findById(invoiceId).get();
        if (invoice.getStatus() == InvoiceStatus.ACTIVE) {
            invoice.setStatus(IN_PROGRESS);
        } else {
            invoice.setStatus(ACTIVE);
        }
        invoiceDao.save(invoice);
    }

    @Transactional
    public void changeInvoiceStatusToSent(int invoiceId) {
        Invoice invoice = invoiceDao.findById(invoiceId).get();
        System.out.println(invoice);
        LOGGER.info("current invoice status:"+invoice.getStatus());
        if (invoice.getStatus() == InvoiceStatus.ISSUED) {
            invoice.setStatus(SENT);
            invoiceDao.save(invoice);
            webSocketDistributeService.sendNewInvoiceNotification(invoice.getContract().getCustomer().getUser().getUsername(),
                    invoiceId);
        } else System.out.println("Status not corresponding to ISSUED !!!");
    }

    public List<Invoice> getInvoicesByStatus(InvoiceStatus invoiceStatus) {
        return invoiceDao.findAllByStatusOrderByIssueDate(invoiceStatus);
    }

    @Transactional
    public void setInvoiceAsPaid(int id) {
        Invoice invoice = invoiceDao.findById(id).get();
        invoice.setStatus(PAID);
        invoiceDao.save(invoice);
        webSocketDistributeService.sendNewInvoicePaidNotification(invoice.getContract().getCompany().getUser().getUsername(),
                id);
    }

    public InvoiceDescriptionPaymentDto setInvoiceDescription(int id){
        Object[] objects = invoiceDao.setInvoiceDescriptionPayment(id).get(0);
        return new InvoiceDescriptionPaymentDto(objects[0].toString(), objects[1].toString());
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

    public List<Invoice> getInvoiceInPeriodService(int contract_id, LocalDate issueDate){
        return invoiceDao.findInvoiceInPeriod(contract_id, issueDate);
    }

    public void setInvoiceBulkAsPaid(List<Integer> idList) {
        for (Integer i: idList){
            setInvoiceAsPaid(i);
        }
    }
}
