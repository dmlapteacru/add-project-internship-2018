package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.Invoice;
import com.endava.addprojectinternship2018.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Integer> {

    Invoice findById(int id);

    List<Invoice> findAll();

    List<Invoice> findByContractId(int id);
//    @Query(value = "FROM User WHERE username=:username")
//@Param("username")
    List<Invoice>findAllByStatus(InvoiceStatus invoiceStatus);

    List<Invoice> findAllByIssueDate(LocalDate localDate);

    List<Invoice> findAllByDueDate(LocalDate localDate);

    List<Invoice> findAllByContractCustomerId(int id);

    List<Invoice> findAllByContract_Company_Name(String name);

}
