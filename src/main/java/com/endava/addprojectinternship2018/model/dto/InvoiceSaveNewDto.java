package com.endava.addprojectinternship2018.model.dto;

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
public class InvoiceSaveNewDto {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @NotNull(message = "must have a value")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;


    private int contractId;

    @NotNull
    private InvoiceStatus status;

    @DecimalMin("0.0")
    private double sum;

}
