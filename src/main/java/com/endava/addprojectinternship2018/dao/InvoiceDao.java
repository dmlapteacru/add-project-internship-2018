package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.InvoiceStatus;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Integer> {

    Invoice findById(int id);

    List<Invoice> findAll();

    List<Invoice> findByContractId(int id);

    List<Invoice>findAllByStatus(InvoiceStatus invoiceStatus);

    List<Invoice> findAllByIssueDate(LocalDate localDate);

    List<Invoice> findAllByDueDate(LocalDate localDate);

    List<Invoice> findAllByContractCustomerId(int id);
}
