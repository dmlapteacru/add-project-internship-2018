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

import java.util.List;
import java.util.Optional;

@Component
public class Validator implements org.springframework.validation.Validator {
    @Autowired
    private CategoryService categoryService;
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(@Nullable Object o, Errors errors) {
        Category category = (Category) o;
        Optional<Category> dbCategory = categoryService.getCategoryByName(category.getName());

        if (category.getName().isEmpty() || category.getDescription().isEmpty()){
            errors.rejectValue("name", "Fields must not be empty.");
        }
        if (dbCategory.isPresent() && dbCategory.get().getId()!=category.getId()){
            errors.rejectValue("name", "Category already exists.");
        }
    }

    public void validateStatementDates(StatementDateReqDto dateReqDto, Errors errors) {
        if (dateReqDto.getDate().isEmpty() || dateReqDto.getDateTo().isEmpty()){
            errors.rejectValue("date", "Dates can't be empty.");
        }
    }

    public void validateInvoicePayment(Double balance, PaymentDto paymentDto, Errors errors) {
        if (paymentDto.getSum() > balance){
            errors.rejectValue("sum", "Not enough money.");
        }
    }

    public void validateBulkInvoicePayment(Double balance, List<PaymentDto> paymentDtoList, Errors errors) {
        double sum = 0;
        for (PaymentDto pdto:paymentDtoList
             ) {
            sum = sum + pdto.getSum();
        }
        if (balance < sum){
//            errors.rejectValue("sum", "Not enough money.");
            errors.reject("Not enough money.");
        }
    }
}
