package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {
    // short names because of encription needs
    private String date; // date in format ('yyyy-MM-dd')
    private long mainCount; // main account
    private long correspondentCount; // correspondent account
    private double sum; // sum
    private String description; // description
}
