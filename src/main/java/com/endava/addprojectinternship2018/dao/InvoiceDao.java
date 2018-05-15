package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.dto.InvoiceCustomerViewDto;
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

    List<Invoice> findAll();

    List<Invoice> findByContractId(int id);

    List<Invoice>findAllByStatus(InvoiceStatus invoiceStatus);

    List<Invoice> findAllByIssueDate(LocalDate localDate);

    List<Invoice> findAllByDueDate(LocalDate localDate);

    List<Invoice> findAllByContractCustomerId(int id);

    List<Invoice> findAllByContract_Company_Name(String name);

    List<Invoice> findAllByContract_Company_IdOrderById(int id);

//    @Query("select new com.endava.addprojectinternship2018.model.dto.InvoiceDescriptionPaymentDto(pr.name, pr.name) " +
//            "from Invoice i " +
//            "left join Contract co on i.contract=co.id " +
////            "left join Company com on co.company=com.id " +
//            "left join Product pr on co.product=pr.id " +
//            "where i.id=:id")
    @Query(value = "SELECT com.name as companyName, pr.name as productName FROM invoice i\n" +
            "LEFT JOIN contract co on i.contract_id=co.id\n" +
            "LEFT JOIN company com on com.id=co.company_id\n" +
            "LEFT JOIN product pr on co.product_id=pr.id\n" +
            "WHERE i.id=:id",nativeQuery = true)
    List<Object[]> setInvoiceDescriptionPayment(@Param("id") int id);


    @Query(value = "SELECT i.id, i.sum, i.issue_date, i.due_date, i.status, com.name as comName, pr.name as prodName FROM invoice i\n" +
            "LEFT JOIN contract co on i.contract_id=co.id\n" +
            "LEFT JOIN company com on com.id=co.company_id\n" +
            "LEFT JOIN product pr on co.product_id=pr.id\n WHERE co.customer_id=:id",nativeQuery = true)
    List<Object[]> findAllInvoiceCustomerViewByCustomerId(@Param(value = "id") int id);

}
