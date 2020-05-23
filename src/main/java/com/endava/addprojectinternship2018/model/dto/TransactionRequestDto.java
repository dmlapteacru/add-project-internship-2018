package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {
    private String Date;
    private String MainCount;
    private String CorrespondentCount;
    private double Sum;
    private String Description;
}
