package com.endava.addprojectinternship2018.model.dto;

import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.model.enums.UserStatus;
import com.endava.addprojectinternship2018.validation.PasswordMatches;
import com.endava.addprojectinternship2018.validation.ValidUsername;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class UserDto {

    private int userId;

    @NotEmpty
    @ValidUsername
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
