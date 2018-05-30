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
    private double balanceBefore; // balance before
    private List<TransactionRequestDto> listOfTransactions;
}
