package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.dao.ContractDao;
import com.endava.addprojectinternship2018.dao.InvoiceDao;
import com.endava.addprojectinternship2018.model.Product;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.dto.InvoiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.endava.addprojectinternship2018.model.enums.InvoiceStatus.ACTIVE;
import static com.endava.addprojectinternship2018.model.enums.InvoiceStatus.IN_PROGRESS;
import static com.endava.addprojectinternship2018.model.enums.InvoiceStatus.PAID;

@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    public List<Invoice> getAllInvoices(){
        return invoiceDao.findAll();
    }

    public Invoice getInvoiceById(int invoiceId){return invoiceDao.findById(invoiceId).get();}

    public List<Invoice> getAllInvoiceByContractId(int id){
        return invoiceDao.findByContractId(id);
    }

    public List<Invoice> getAllInvoicesByStatus(InvoiceStatus status){
        return invoiceDao.findAllByStatus(status);
    }

    public List<Invoice> getAllInvoicesByIssueDate(LocalDate localDate){
        return invoiceDao.findAllByIssueDate(localDate);
    }

    public List<Invoice> getAllInvoicesByDueDate(LocalDate localDate){
        return invoiceDao.findAllByDueDate(localDate);
    }

    public List<Invoice> getInvoicesByCustomerId(int id) {
        return invoiceDao.findAllByContractCustomerId(id);
    }

    public List<Invoice> getInvoicesByCompanyId(int id){

        return invoiceDao.findByContractId(id);
    }

    public List<Invoice> getInvoicesByCompany(String name){
        return invoiceDao.findAllByContract_Company_Name(name);
    };

    public void save(Invoice invoice){
        invoiceDao.save(invoice);
    }

    public void saveDto(InvoiceDto invoiceDto){
        Invoice invoice = new Invoice(invoiceDto.getSum(), invoiceDto.getIssueDate()
                , invoiceDto.getDueDate(), invoiceDto.getStatus(), invoiceDto.getContract());
        invoiceDao.save(invoice);
    }

    public void deleteInvoice(int id){
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

    public List<Invoice> getInvoicesByStatus(InvoiceStatus invoiceStatus){
        return invoiceDao.findAllByStatus(invoiceStatus);
    }

    public void setInvoiceAsPaid(int id){
        Invoice invoice = invoiceDao.findById(id).get();
        invoice.setStatus(PAID);
        invoiceDao.save(invoice);
    }

}
