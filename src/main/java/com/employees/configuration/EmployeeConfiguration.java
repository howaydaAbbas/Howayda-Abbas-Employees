package com.employees.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author HowaydaGamal
 * @created 1/6/2022
 */

@Data
@Configuration
public class EmployeeConfiguration {

    @Value("${employee.current.date.to}")
    private String currentDateTo;

    @Value("${employee.header.date.to}")
    private String dateToHeader;
}
