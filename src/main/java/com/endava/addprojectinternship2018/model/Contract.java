package com.endava.addprojectinternship2018.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name = "CONTRACT")
@Data
@NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private LocalDate issueDate;

    @Column
    private LocalDate expireDate;

    @Column
    private double sum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Contract(LocalDate issueDate, LocalDate expireDate, double sum) {
        this.issueDate = issueDate;
        this.expireDate = expireDate;
        this.sum = sum;
    }
}
