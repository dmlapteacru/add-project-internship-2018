package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {
    // short names because of encription needs
    private String d; // date in format ('yyyy-MM-dd')
    private long m; // main account
    private long c; // correspondent account
    private double s; // sum
    private String dr; // description
}
