package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.model.enums.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedFilter {

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    private double sumFrom;
    private double sumTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    public AdvancedFilter(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public AdvancedFilter(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}
