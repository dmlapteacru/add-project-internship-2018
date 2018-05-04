package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.Contract;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import com.endava.addprojectinternship2018.util.LocalDateTimeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Convert;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class InvoiceDto {

    private int invoiceId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime issueDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dueDate;

    @NotNull
    private Contract contract;

    private InvoiceStatus status;

    @DecimalMin("0.0")
    private double sum;

    public Contract getContract() {
        return contract;
    }

    public InvoiceDto(LocalDateTime issueDate,LocalDateTime dueDate, @DecimalMin("0.0") double sum) {
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.sum = sum;
    }
}
