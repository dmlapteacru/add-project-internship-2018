package com.endava.addprojectinternship2018.model;

import com.endava.addprojectinternship2018.model.Enums.InvoiceStatus;
import com.endava.addprojectinternship2018.util.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column
    private double sum;

    @Column
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime issueDate;

    @Column
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

}