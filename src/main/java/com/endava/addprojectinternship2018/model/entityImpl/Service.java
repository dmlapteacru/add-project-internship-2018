package com.endava.addprojectinternship2018.model.entityImpl;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "description")
    private String description;

    public Service(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
