package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Integer> {

    Optional<Invoice> findById(int id);

    List<Invoice> findAllByContractId(int id);

    List<Invoice> findAll();

    List<Invoice> findAllByContractCustomerIdAndStatusIsInOrderByIssueDate(int customerId, List<InvoiceStatus> statuses);

    List<Invoice> findAllByContract_Company_IdOrderById(int id);

    @Query("select inv from Invoice inv join inv.contract contr where inv.serviceEndDate >=:date AND contr.id =:contract_id")
    List<Invoice> findInvoiceInPeriod(@Param("contract_id")int contract_id, @Param(  "date")LocalDate date);

    long countAllByContractCustomerIdAndStatus(int customerId, InvoiceStatus status);

    long countAllByContractCompanyIdAndStatus(int companyId, InvoiceStatus status);

    long countAllByContractId(int contractId);

    List<Invoice> findAllByContractCustomerIdAndStatusAndSumBetweenAndIssueDateBetweenOrderByIssueDate
            (int customerId, InvoiceStatus status, double sumFrom, double sumTo, LocalDate dateFrom, LocalDate dateTo);

    List<Invoice> findAllByContractCompanyIdAndStatusAndSumBetweenAndIssueDateBetweenOrderByIssueDate
            (int companyId, InvoiceStatus status, double sumFrom, double sumTo, LocalDate dateFrom, LocalDate dateTo);

    List<Invoice> findAllByContractCompanyIdAndSumBetweenAndIssueDateBetweenOrderByIssueDate
            (int companyId, double sumFrom, double sumTo, LocalDate dateFrom, LocalDate dateTo);

    List<Invoice> findAllByContractCustomerIdAndStatusInAndSumBetweenAndIssueDateBetweenOrderByIssueDate
            (int customerId, List<InvoiceStatus> statuses, double sumFrom, double sumTo, LocalDate dateFrom, LocalDate dateTo);


}
