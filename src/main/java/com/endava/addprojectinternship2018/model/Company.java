package com.endava.addprojectinternship2018.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "COMPANY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String name;

    @Column
    private String email;

    @Column
    private String accountNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "company_prices",
            joinColumns = {@JoinColumn(name = "company_id")},
            inverseJoinColumns = {@JoinColumn(name = "price_id")}
            )
    private List<Price> prices;

    @OneToMany(mappedBy = "company")
    private List<Contract> contracts;

}
