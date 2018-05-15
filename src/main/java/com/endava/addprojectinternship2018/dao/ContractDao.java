package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractDao extends JpaRepository<Contract, Integer> {

    Optional<Contract> findById(int id);

    Optional<Contract> findByCustomerIdAndCompanyIdAndProductId(int customerId, int companyId, int productId);

    List<Contract> findAll();

    List<Contract> findAllByCompanyName(String companyName);

    List<Contract> findAllByCompanyId(int id);

    List<Contract> findAllByCustomerId(int id);

    List<Contract> findAllByProductId(int productId);

    long countByCompanyIdAndStatus(int companyId, ContractStatus status);

    long countByCustomerIdAndStatus(int customerId, ContractStatus status);

    List<Contract> findAllByCompanyIdAndSumBetweenAndIssueDateBetween(int id,
                                                                      double sumFrom, double sumTo,
                                                                      LocalDate dateFrom, LocalDate dateTo);

    List<Contract> findAllByCustomerIdAndSumBetweenAndIssueDateBetween(int id,
                                                                       double sumFrom, double sumTo,
                                                                       LocalDate dateFrom, LocalDate dateTo);

    List<Contract> findAllByCustomerIdAndStatusAndSumBetweenAndIssueDateBetween(int id, ContractStatus status,
                                                                                double sumFrom, double sumTo,
                                                                                LocalDate dateFrom, LocalDate dateTo);

    List<Contract> findAllByCompanyIdAndStatusAndSumBetweenAndIssueDateBetween(int id, ContractStatus status,
                                                                               double sumFrom, double sumTo,
                                                                               LocalDate dateFrom, LocalDate dateTo);

}

