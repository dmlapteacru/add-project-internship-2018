package com.endava.addprojectinternship2018.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomUsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private Pattern pattern;
    private Matcher matcher;
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9._-]{3,18}$";

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return (validateUsername(username));
    }

    private boolean validateUsername(String email) {
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
