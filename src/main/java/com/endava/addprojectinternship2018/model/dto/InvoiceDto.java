package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class InvoiceDto {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @Range(min = 1)
    private int contractId;

    private Contract contract;

    @DecimalMin("0.0")
    private double sum;

    public Contract getContract(int contractId) {
        return contract;
    }
}
