package com.endava.addprojectinternship2018.dao;

import com.endava.addprojectinternship2018.model.Transaction;
import org.h2.mvstore.db.TransactionStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionDao extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByBankAccountPayer_AccountNumberAndDateBetweenOrderByIdAsc(String accountName, LocalDate from, LocalDate to);
}
