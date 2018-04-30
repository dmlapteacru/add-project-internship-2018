package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class InvoiceDto {


    private int invoiceId;

    private LocalDate issueDate;

    private LocalDate dueDate;

    private int contractId;

    private Contract contract;

    @NotEmpty
    private double sum;

    @NotEmpty
    private InvoiceStatus status;

    public Contract getContract(int contractId) {
        return contract;
    }
}
