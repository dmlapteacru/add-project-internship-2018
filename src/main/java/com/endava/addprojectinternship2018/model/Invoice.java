package com.endava.addprojectinternship2018.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.security.PrivateKey;

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
    private double sum;

    @Column
    private LocalDate issueDate;

    @Column
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

}