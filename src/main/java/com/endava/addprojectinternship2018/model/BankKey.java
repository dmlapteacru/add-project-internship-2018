package com.endava.addprojectinternship2018.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BANK_KEY")
public class BankKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private byte[] modulus;

    @Column
    private byte[] privateKey;

    public BankKey(byte[] modulus, byte[] privateKey) {
        this.modulus = modulus;
        this.privateKey = privateKey;
    }
}
