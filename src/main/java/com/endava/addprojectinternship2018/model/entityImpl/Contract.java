package com.endava.addprojectinternship2018.model.entityImpl;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contract")
@Data
@NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // String field with some additional contract nr. ???

    @Column(name = "customer")
    private Customer customer;

    @Column(name = "company")
    private Company company;

    @Column(name = "service")
    private Service service;

    @Column(name = "issueDate")
    private LocalDate issueDate;

    @Column(name = "expireDate")
    private LocalDate expireDate;

    public Contract(Customer customer, Company company
            , Service service, LocalDate issueDate
            , LocalDate expireDate) {

        this.customer = customer;
        this.company = company;
        this.service = service;
        this.issueDate = issueDate;
        this.expireDate = expireDate;

    }
}
