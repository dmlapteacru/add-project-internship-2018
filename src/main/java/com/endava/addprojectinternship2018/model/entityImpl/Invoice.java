package com.endava.addprojectinternship2018.model.entityImpl;

import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.model.enums.Status;
import com.endava.addprojectinternship2018.model.entityIntf.ContractIntf;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // String field with some additional contract nr. ???

    @Column(name = "contract")
    private Contract contract;

    @Column(name = "sum")
    private double sum;

    @Column(name = "service")
    private Service service;

    @Column(name = "issueDate")
    private LocalDate issueDate;

    @Column(name = "dueDate")
    private LocalDate dueDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

}