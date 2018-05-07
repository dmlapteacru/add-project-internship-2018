package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.enums.ContractStatus;
import com.endava.addprojectinternship2018.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto {

    private int contractId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expireDate;

    @DecimalMin("0.0")
    private double sum;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    private List<Company> companies;

    @NotNull
    private Company selectedCompany;

    private List<Customer> customers;

    @NotNull
    private Customer selectedCustomer;

    private List<Product> products;

    @NotNull
    private Product selectedProduct;

}
