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
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto {

    private int contractId;

    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime issueDate;

    @NotEmpty
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expireDate;

    @NotEmpty
    private double sum;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    private List<Company> companies;

    @NotEmpty
    private Company selectedCompany;

    private List<Customer> customers;

    @NotEmpty
    private Customer selectedCustomer;

    private List<Product> products;

    @NotEmpty
    private Product selectedProduct;

    public ContractDto(@NotEmpty LocalDateTime issueDate, @NotEmpty LocalDateTime expireDate,
                       @NotEmpty double sum) {
        this.issueDate = issueDate;
        this.expireDate = expireDate;
        this.sum = sum;
    }
}
