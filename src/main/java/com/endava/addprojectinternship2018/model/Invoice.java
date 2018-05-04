package com.endava.addprojectinternship2018.model;

import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.util.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

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

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime issueDate;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dueDate;

    @Column
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    public Invoice(double sum, LocalDateTime issueDate, LocalDateTime dueDate) {
        this.sum = sum;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public Invoice(double sum, LocalDateTime issueDate, LocalDateTime dueDate, InvoiceStatus status, Contract contract) {
        this.sum = sum;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.status = status;
        this.contract = contract;
    }
}