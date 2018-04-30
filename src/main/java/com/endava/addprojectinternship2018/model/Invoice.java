package com.endava.addprojectinternship2018.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
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

    @Column
//    @Pattern(regexp = "[0-9]+[\\.]+[0-9]", message = "Do not match the numerical pattern")
    private double sum;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @Column
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