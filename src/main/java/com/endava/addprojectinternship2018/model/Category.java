package com.endava.addprojectinternship2018.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY")
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    public Category(String name) {
        this.name = name;
    }

}
