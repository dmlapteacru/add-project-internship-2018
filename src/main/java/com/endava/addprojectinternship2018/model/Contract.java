package com.endava.addprojectinternship2018.model;

import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.util.LocalDateTimeConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CONTRACT")
@Data
@NoArgsConstructor
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime issueDate;

    @Column
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime expireDate;

    @Column
    private double sum;

    @Column
    private ContractStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public Contract(LocalDateTime issueDate, LocalDateTime expireDate, double sum) {
        this.issueDate = issueDate;
        this.expireDate = expireDate;
        this.sum = sum;
    }
}
