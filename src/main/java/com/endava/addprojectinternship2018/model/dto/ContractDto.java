package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;
import javax.validation.constraints.NotEmpty;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractDto {

    @NotEmpty
    private LocalDate issueDate;

    @NotEmpty
    private LocalDate expireDate;

    @NotEmpty
    private double sum;

    private Company company;
    private Customer customer;

    @NotEmpty
    private Product product;

}
