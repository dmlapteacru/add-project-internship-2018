package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "CREDENTIALS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private long countNumber;

    @Column
    private long accessKey;
}
