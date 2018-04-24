package com.endava.addprojectinternship2018.model.dto;


import com.endava.addprojectinternship2018.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegistrationDto extends UserRegistrationDto {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @ValidEmail
    private String email;

}
