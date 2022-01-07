package com.employees.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author HowaydaGamal
 * @created 1/6/2022
 */
@Data
@AllArgsConstructor
public class Employee {

    private String id;
    private String projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

}
