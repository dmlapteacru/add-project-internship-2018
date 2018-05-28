package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementDateReqDto {
    private String date;
    private String dateTo;
    private int pages;
}
