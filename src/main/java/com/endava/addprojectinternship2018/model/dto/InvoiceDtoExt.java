package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.Enums.InvoiceStatus;

import java.time.LocalDateTime;

public class InvoiceDtoExt {
    private int invoiceId;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private String productName;
    private String customerFullName;
    private InvoiceStatus invoiceStatus;
    private double invoiceSum;


}
