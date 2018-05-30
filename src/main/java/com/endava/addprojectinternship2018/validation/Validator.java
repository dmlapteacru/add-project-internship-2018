package com.endava.addprojectinternship2018.validation;

import com.endava.addprojectinternship2018.model.Category;
import com.endava.addprojectinternship2018.model.dto.PaymentDto;
import com.endava.addprojectinternship2018.model.dto.StatementDateReqDto;
import com.endava.addprojectinternship2018.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class Validator implements org.springframework.validation.Validator {

    private final CategoryService categoryService;

    @Autowired
    public Validator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(@Nullable Object o, Errors errors) {
        Category category = (Category) o;
        Optional<Category> dbCategory = categoryService.getCategoryByName(category.getName());

        if (category.getName().isEmpty() || category.getDescription().isEmpty()) {
            errors.rejectValue("name", "Fields must not be empty.");
        }
        if (dbCategory.isPresent() && dbCategory.get().getId() != category.getId()) {
            errors.rejectValue("name", "Category already exists.");
        }
    }

    public void validateStatementDates(StatementDateReqDto dateReqDto, Errors errors) {
        if (dateReqDto.getDate().isEmpty() || dateReqDto.getDateTo().isEmpty()) {
            errors.rejectValue("date", "Dates can not be empty.");
        }
    }

    public List<String> validateInvoicePayment(double balance, List<PaymentDto> paymentDtoList) {

        List<String> errorList = new ArrayList<>();
        if (paymentDtoList.stream().mapToDouble(PaymentDto::getSum).sum() > balance) {
            errorList.add("Not enough money");
        }
        if (paymentDtoList.size() > 5) {
            errorList.add("Impossible to process more than 5 invoices");
        }
        for (PaymentDto paymentDto : paymentDtoList) {
            if (paymentDto.getCorrespondentCount() == null) {
                errorList.add(paymentDto.getDescription());
            }
        }
        return errorList;
    }

}
