package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDescriptionPaymentDto {
    private String companyName;
    private String productName;

    public String getFullDescription(){
        return "Company : "+  companyName + " for service "+ productName;
    }
}
