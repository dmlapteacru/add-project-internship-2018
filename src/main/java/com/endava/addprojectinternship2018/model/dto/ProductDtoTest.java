package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDtoTest {

    @NotNull
    private String name;

    @Min(0)
    private double price;

    @NotNull
    private int companyId;

    @NotNull
    private int categoryId;

    private String description;
}
