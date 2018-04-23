package com.endava.addprojectinternship2018.model.entityImpl;

import com.endava.addprojectinternship2018.model.entityIntf.CompanyIntf;
import com.endava.addprojectinternship2018.model.entityIntf.ServiceIntf;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
public class Company implements CompanyIntf {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private User user;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "email")
    private String email;

    // name ???
    @Column(name = "accNumber")
    private long accountNumber;

    @ManyToMany
    private Set<Service> serviceSet;

    @ManyToMany
    private Set<Contract> contractSet;

    @ManyToMany
    private Set<Invoice> invoiceSet;

    public Company(User user, String name
            , String email, long accountNumber
            , Set<Service> serviceSet, Set<Contract> contractSet
            , Set<Invoice> invoiceSet) {

        this.user = user;
        this.name = name;
        this.email = email;
        this.accountNumber = accountNumber;
        this.serviceSet = serviceSet;
        this.contractSet = contractSet;
        this.invoiceSet = invoiceSet;

    }
}
