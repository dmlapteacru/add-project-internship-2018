package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRegistrationDto extends UserRegistrationDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

}
