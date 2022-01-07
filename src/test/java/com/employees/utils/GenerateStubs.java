package com.employees.utils;

import com.employees.model.Employee;

import java.time.LocalDate;

/**
 * @author HowaydaGamal
 * @created 1/7/2022
 */
public class GenerateStubs {

    public static final String EMPLOYEE_AS_ROW = "1,1,2000-01-01,2010-01-01";

    public static Employee generateEmployee(){
        return new Employee("1", "1", LocalDate.parse("2000-01-01"), LocalDate.parse("2010-01-01"));
    }
}
