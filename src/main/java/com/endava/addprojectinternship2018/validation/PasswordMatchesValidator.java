package com.endava.addprojectinternship2018.validation;

import com.endava.addprojectinternship2018.model.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        UserDto user = (UserDto) obj;

        boolean regExpMatches = user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        if (!regExpMatches) {
            context.buildConstraintViolationWithTemplate("Password is not strong!")
                    .addPropertyNode("password")
                    .addConstraintViolation();
            return false;
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            context.buildConstraintViolationWithTemplate("Password and password confirmation are not equals!")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

}
