package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.InvoiceDao;
import com.endava.addprojectinternship2018.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
// ??
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    public Invoice getInvoiceById(int id){
        return invoiceDao.findById(id);
    }

    private List<Invoice> getAllInvoices(){
        return invoiceDao.findAll();
    }

    private List<Invoice> getAllInvoiceByContract(int id){
        return invoiceDao.findAllByContractId(id);
    }

    private List<Invoice> getAllInvoicesByStatus(String status){
        return invoiceDao.findAllByStatus(status);
    }

    private List<Invoice> getAllInvoicesByIssueDate(LocalDate localDate){
        return invoiceDao.findAllByIssueDate(localDate);
    }

    private List<Invoice> getAllInvoicesByDueDate(LocalDate localDate){
        return invoiceDao.findAllByDueDate(localDate);
    }


}
