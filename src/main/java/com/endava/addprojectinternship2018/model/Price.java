package com.endava.addprojectinternship2018.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PRICE")
@Data
@NoArgsConstructor
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @Column
    private double price;

    @ManyToMany(mappedBy = "prices")
    private List<Company> companies;


}
