package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDtoExt {
    private int invoiceId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String productName;
    private String customerFullName;
    private InvoiceStatus invoiceStatus;
    private double invoiceSum;


}
