package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.dto.InvoiceCustomerViewDto;
import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.dto.InvoiceDescriptionPaymentDto;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.Invoice;
import org.springframework.data.domain.Sort;
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

    Optional<Invoice> findByContractId(int id);

    List<Invoice> findAllByContractId(int id);

    List<Invoice> findAll();

    List<Invoice> findByContractIdOrderByIssueDate(int id);

    List<Invoice> findAllByStatusOrderByIssueDate(InvoiceStatus invoiceStatus);

    List<Invoice> findAllByIssueDate(LocalDate localDate);

    List<Invoice> findAllByDueDate(LocalDate localDate);

    List<Invoice> findAllByContractCustomerIdOrderByIssueDate(int id);

    List<Invoice> findAllByContractCustomerIdAndStatusIsInOrderByIssueDate(int customerId, List<InvoiceStatus> statuses);

    List<Invoice> findAllByContract_Company_Name(String name);

    List<Invoice> findAllByContract_Company_IdOrderById(int id);

    @Query("select inv from Invoice inv join inv.contract contr where inv.serviceEndDate >=:date AND contr.id =:contract_id")
    List<Invoice> findInvoiceInPeriod(@Param("contract_id")int contract_id, @Param(  "date")LocalDate date);

    @Query(value = "SELECT com.name as companyName, pr.name as productName FROM invoice i\n" +
            "LEFT JOIN contract co on i.contract_id=co.id\n" +
            "LEFT JOIN company com on com.id=co.company_id\n" +
            "LEFT JOIN product pr on co.product_id=pr.id\n" +
            "WHERE i.id=:id",nativeQuery = true)
    List<Object[]> setInvoiceDescriptionPayment(@Param("id") int id);

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
