package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBankAccountDto {

    private Long countNumber;
    private byte[] modulus;
    private byte[] privateKey;
    private String bankPublicKeyModulus;

    public UserBankAccountDto(Long countNumber, byte[] modulus, byte[] privateKey) {
        this.countNumber = countNumber;
        this.modulus = modulus;
        this.privateKey = privateKey;
    }
}
