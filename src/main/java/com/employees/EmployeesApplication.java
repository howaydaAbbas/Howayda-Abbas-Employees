package com.employees;

import com.employees.exception.DateParsingException;
import com.employees.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author HowaydaGamal
 * @created 1/6/2022
 */

@SpringBootApplication
@AllArgsConstructor
public class EmployeesApplication {

    private final EmployeeService employeeService;

    public static void main(String[] args) {
        SpringApplication.run(EmployeesApplication.class, args);
    }

    @PostConstruct
    public void runAfterObjectCreated() throws IOException, DateParsingException {
        employeeService.parseEmployeesFile();
    }
}
