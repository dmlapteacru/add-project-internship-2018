package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementDto {
    private double startBalance; // balance before
    private double endBalance; // balance after
    private List<TransactionDto> listOfTransactions;
}
