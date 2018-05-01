package com.endava.addprojectinternship2018.model;

import com.endava.addprojectinternship2018.model.Enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.security.PrivateKey;
import java.time.LocalDate;

@Entity
@Table(name = "INVOICE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @DecimalMin("0.0")
    private double sum;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @Column
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    public Invoice(double sum, LocalDate issueDate, LocalDate dueDate) {
        this.sum = sum;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public Invoice(double sum, LocalDate issueDate, LocalDate dueDate, InvoiceStatus status, Contract contract) {
        this.sum = sum;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
        this.contract = contract;
    }
}