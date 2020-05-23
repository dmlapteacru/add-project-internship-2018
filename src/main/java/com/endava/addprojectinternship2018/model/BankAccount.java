package com.endava.addprojectinternship2018.model;

import com.endava.addprojectinternship2018.util.BankServiceUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

import static com.endava.addprojectinternship2018.util.BankServiceUtils.generateAccountNumber;

@Data
@AllArgsConstructor
@Entity(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private Double balance;

    @OneToMany
    private Set<Transaction> transactions;

    public BankAccount() {
        this.accountNumber = generateAccountNumber();
        this.balance = 0.0;
    }

    public void reduceBalance(double amount) {
        this.balance = this.balance - amount;
    }

    public void increaseBalance(double amount) {
        this.balance = this.balance + amount;
    }

}
