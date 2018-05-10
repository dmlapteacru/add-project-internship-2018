package com.endava.addprojectinternship2018.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "BANK_ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column
    private long countNumber;

    @Column
    private long accessKey;
}
