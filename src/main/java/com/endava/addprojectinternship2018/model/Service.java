package com.endava.addprojectinternship2018.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SERVICE")
@Data
@NoArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(unique = true)
    private String name;

    @Column
    private double price;

    @Column
    private String description;

    @ManyToMany(mappedBy = "services")
    private List<Company> companies;

    public Service(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

}
