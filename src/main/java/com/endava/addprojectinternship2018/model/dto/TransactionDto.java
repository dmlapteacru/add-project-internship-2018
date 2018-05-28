package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String date; // ("yyyy-MM-dd")
    private String partnerName;
    private double sum;
    private double currentBalance;
    private String description;
}
