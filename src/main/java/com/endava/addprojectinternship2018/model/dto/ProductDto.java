package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.Category;
import com.endava.addprojectinternship2018.model.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private int productId;

    @NotEmpty
    private String name;

    @NotEmpty
    private Category category;

    @NotEmpty
    private double price;

    @NotEmpty
    private Company company;

    private String description;

}
