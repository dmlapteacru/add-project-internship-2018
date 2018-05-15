package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.dao.ContractDao;
import com.endava.addprojectinternship2018.dao.InvoiceDao;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.dto.InvoiceCustomerViewDto;
import com.endava.addprojectinternship2018.model.dto.AdvancedFilter;
import com.endava.addprojectinternship2018.model.dto.InvoiceDescriptionPaymentDto;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.dto.InvoiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.endava.addprojectinternship2018.model.enums.InvoiceStatus.ACTIVE;
import static com.endava.addprojectinternship2018.model.enums.InvoiceStatus.IN_PROGRESS;
import static com.endava.addprojectinternship2018.model.enums.InvoiceStatus.PAID;

@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    public List<Invoice> getAllInvoices() {
        return invoiceDao.findAll();
    }

    public Invoice getInvoiceById(int invoiceId) {
        return invoiceDao.findById(invoiceId).get();
    }

    public List<Invoice> getAllInvoiceByContractId(int id) {
        return invoiceDao.findByContractId(id);
    }

    public List<Invoice> getAllInvoicesByStatus(InvoiceStatus status) {
        return invoiceDao.findAllByStatus(status);
    }

    public List<Invoice> getAllInvoicesByIssueDate(LocalDate localDate) {
        return invoiceDao.findAllByIssueDate(localDate);
    }

    public List<Invoice> getAllInvoicesByDueDate(LocalDate localDate) {
        return invoiceDao.findAllByDueDate(localDate);
    }

    public List<Invoice> getInvoicesByCustomerId(int id) {
        return invoiceDao.findAllByContractCustomerId(id);
    }

    public List<Invoice> getInvoicesByCompanyId(int id) {
        return invoiceDao.findAllByContract_Company_IdOrderById(id);
    }

    public List<Invoice> getInvoicesByCompany(String name) {
        return invoiceDao.findAllByContract_Company_Name(name);
    }

    public void save(Invoice invoice) {
        invoiceDao.save(invoice);
    }

    public void saveDto(InvoiceDto invoiceDto) {
        Invoice invoice = new Invoice(invoiceDto.getSum(), invoiceDto.getIssueDate()
                , invoiceDto.getDueDate(), invoiceDto.getStatus(), invoiceDto.getContract());
        invoiceDao.save(invoice);
    }

    public void deleteInvoice(int id) {
        invoiceDao.deleteById(id);
    }

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

    public InvoiceDto convertInvoiceToInvoiceDto(Invoice invoice) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setContract(invoice.getContract());
        invoiceDto.setDueDate(invoice.getDueDate());
        invoiceDto.setIssueDate(invoice.getIssueDate());
        invoiceDto.setStatus(invoice.getStatus());
        invoiceDto.setSum(invoice.getSum());
        return invoiceDto;
    }

    public void changeInvoiceStatus(int invoiceId) {
        Invoice invoice = invoiceDao.findById(invoiceId).get();
        if (invoice.getStatus() == InvoiceStatus.ACTIVE) {
            invoice.setStatus(IN_PROGRESS);
        } else invoice.setStatus(ACTIVE);
    }

    public List<Invoice> getInvoicesByStatus(InvoiceStatus invoiceStatus) {
        return invoiceDao.findAllByStatus(invoiceStatus);
    }

    public void setInvoiceAsPaid(int id) {
        Invoice invoice = invoiceDao.findById(id).get();
        invoice.setStatus(PAID);
        invoiceDao.save(invoice);
    }

    public InvoiceDescriptionPaymentDto setInvoiceDescription(int id){
        Object[] objects = invoiceDao.setInvoiceDescriptionPayment(id).get(0);
        return new InvoiceDescriptionPaymentDto(objects[0].toString(), objects[1].toString());
    }

    public List<InvoiceCustomerViewDto> getInvoiceCustomerViewByCutomerId(int id){
        List<InvoiceCustomerViewDto> invoiceCustomerViewDtoList = new ArrayList<>();
        List<Object[]> objects = invoiceDao.findAllInvoiceCustomerViewByCustomerId(id);
        for (Object[] obj:objects
             ) {
            invoiceCustomerViewDtoList.add(new InvoiceCustomerViewDto(
                    (Integer)obj[0],
                    (Double)obj[1],
                    (Date)obj[2],
                    (Date)obj[3],
                    obj[4].toString(),
                    obj[5].toString(),
                    obj[6].toString()));
        }
        return invoiceCustomerViewDtoList;
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
            return invoiceDao.findAllByContractCustomerIdAndSumBetweenAndIssueDateBetween(customerId,
                    sumFrom, sumTo, dateFrom, dateTo);
        }
        return invoiceDao.findAllByContractCustomerIdAndStatusAndSumBetweenAndIssueDateBetween(customerId,
                filter.getInvoiceStatus(), sumFrom, sumTo, dateFrom, dateTo);
    }

    public List<Invoice> getInvoicesByCompanyIdFiltered(int companyId, AdvancedFilter filter) {
        double sumFrom = (filter.getSumFrom() == 0 ? Double.MIN_VALUE : filter.getSumFrom());
        double sumTo = (filter.getSumTo() == 0 ? Double.MAX_VALUE : filter.getSumTo());
        LocalDate dateFrom = (filter.getDateFrom() == null ? LocalDate.of(1, 1, 1) : filter.getDateFrom());
        LocalDate dateTo = (filter.getDateTo() == null ? LocalDate.of(4999, 12, 31) : filter.getDateTo());

        if (filter.getInvoiceStatus() == null) {
            return invoiceDao.findAllByContractCompanyIdAndSumBetweenAndIssueDateBetween(companyId,
                    sumFrom, sumTo, dateFrom, dateTo);
        }
        return invoiceDao.findAllByContractCompanyIdAndStatusAndSumBetweenAndIssueDateBetween(companyId,
                filter.getInvoiceStatus(), sumFrom, sumTo, dateFrom, dateTo);
    }

}
