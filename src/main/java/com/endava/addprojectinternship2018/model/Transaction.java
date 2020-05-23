package com.endava.addprojectinternship2018.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccountPayer;

    @ManyToOne
    @JoinColumn(name = "bank_account_id_receiver")
    private BankAccount bankAccountReceiver;

    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private LocalDate date;

    private Double balanceBefore;

    private Double sum;

    private Double currentBalance;

    private String description;

    public Transaction(BankAccount bankAccountPayer, BankAccount bankAccountReceiver, Double balanceBefore, Double sum, String description) {
        this.bankAccountPayer = bankAccountPayer;
        this.bankAccountReceiver = bankAccountReceiver;
        this.balanceBefore = balanceBefore;
        this.sum = sum;
        this.currentBalance = balanceBefore + sum;
        this.description = description;
        this.date = LocalDate.now();
    }
}
