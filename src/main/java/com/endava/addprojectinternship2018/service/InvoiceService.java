package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.dao.ContractDao;
import com.endava.addprojectinternship2018.dao.InvoiceDao;
import com.endava.addprojectinternship2018.model.Enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.dto.InvoiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.endava.addprojectinternship2018.model.Enums.InvoiceStatus.ACTIVE;


@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private ContractDao contractDao;

    public List<Invoice> getAllInvoices(){
        return invoiceDao.findAll();
    }

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
                , invoiceDto.getDueDate(), ACTIVE, invoiceDto.getContract(invoiceDto.getContractId()));
        invoiceDao.save(invoice);
    }

    public void deleteInvoice(int id){
        invoiceDao.deleteById(id);
    }
}
