package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementRequestDto {
    // short names because of encription needs
    private int p; // pages
    private double bf; // balance before
    private double ba; // balance after
    private double bfc; // balance before current
    private List<TransactionRequestDto> listOfTransactions;
}
