package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

    private int companyId;

    @NotEmpty
    private String name;

    @NotEmpty
    @ValidEmail
    private String email;

    private String accountNumber;

    @Valid
    private UserDto userDto;

}
