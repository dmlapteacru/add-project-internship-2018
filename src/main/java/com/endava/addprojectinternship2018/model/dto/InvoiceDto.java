package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {

    private int invoiceId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull
    private Contract contract;

    private InvoiceStatus status;

    @DecimalMin("0.0")
    private double sum;

    public Contract getContract() {
        return contract;
    }

    public InvoiceDto(LocalDate issueDate,LocalDate dueDate, @DecimalMin("0.0") double sum) {
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.sum = sum;
    }
}
