package com.endava.addprojectinternship2018.model;

import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.util.LocalDateConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
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

    @DecimalMin("0.0")
    private double sum;

    @Convert(converter = LocalDateConverter.class)
    private LocalDate issueDate;

    @Convert(converter = LocalDateConverter.class)
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