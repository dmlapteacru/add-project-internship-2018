package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Integer> {
    Invoice findById(int id);

    @Override
    List<Invoice> findAll();
    List<Invoice> findAllByContractId(int id);
    List<Invoice>findAllByStatus(String status);
    List<Invoice> findAllByIssueDate(LocalDate localDate);
    List<Invoice> findAllByDueDate(LocalDate localDate);

}
