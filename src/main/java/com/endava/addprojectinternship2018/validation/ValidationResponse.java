package com.endava.addprojectinternship2018.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {
    private String status;
    private List<ErrorMessage> errorMessageList;
}
