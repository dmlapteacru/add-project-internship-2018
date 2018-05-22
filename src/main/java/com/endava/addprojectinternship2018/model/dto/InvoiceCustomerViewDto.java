package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceCustomerViewDto {
    private int id;
    private double sum;
    private Date issueDate;
    private Date dueDate;
    private String status;
    private String companyName;
    private String productName;
}
