package com.endava.addprojectinternship2018.model.entityImpl;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne // ???
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @ManyToMany // ???
    private Set<Contract> contractSet;

    @ManyToMany // ???
    private Set<Invoice> invoiceSet;

    // long or String ???
    @Column(name = "bankAccount")
    private String bankAccount;

    public Customer(User user, String firstName
            , String lastName, String email
            , Set<Contract> contractSet
            , Set<Invoice> invoiceSet, String bankAccount) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contractSet = contractSet;
        this.invoiceSet = invoiceSet;
        this.bankAccount = bankAccount;
    }
}
