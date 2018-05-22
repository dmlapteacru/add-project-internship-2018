package com.endava.addprojectinternship2018.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatementDto {
    private LocalDate date;
    private Long mainCount;
    private Long correspondentCount;
    private Long sum;
    private String description;
}
