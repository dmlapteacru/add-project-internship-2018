package com.endava.addprojectinternship2018.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
@Data
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_category_id")
    private Category category;

    @Column
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

}
