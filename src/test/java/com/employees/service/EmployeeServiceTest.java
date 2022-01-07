package com.employees.service;

import com.employees.EmployeesApplication;
import com.employees.configuration.EmployeeConfiguration;
import com.employees.exception.DateParsingException;
import com.employees.model.Employee;
import com.employees.utils.GenerateStubs;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author HowaydaGamal
 * @created 1/7/2022
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EmployeesApplication.class})
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeConfiguration employeeConfiguration;

    @Test
    public void calculateNumberOfDaysAsTeamSuccessT() {

        Employee firstEmployee = GenerateStubs.generateEmployee();
        Employee secondEmployee = new Employee("11", "1", LocalDate.parse("2005-03-20"), LocalDate.parse("2012-10-11"));
        long numberOfDaysAsTeam = ChronoUnit.DAYS.between(secondEmployee.getDateFrom(), firstEmployee.getDateTo());
        Assertions.assertEquals(numberOfDaysAsTeam, employeeService.calculateNumberOfDaysAsTeam(firstEmployee, secondEmployee));
    }

    @Test
    public void parseEmployeeDataSuccessT() throws DateParsingException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] empArray = GenerateStubs.EMPLOYEE_AS_ROW.split(",");
        Employee employee = new Employee(empArray[0], empArray[1], LocalDate.parse(empArray[2], dateTimeFormatter)
                , empArray[3].equals(employeeConfiguration.getCurrentDateTo()) ? LocalDate.now() : LocalDate.parse(empArray[3], dateTimeFormatter));
        Assertions.assertEquals(employeeService.parseEmployeeData(GenerateStubs.EMPLOYEE_AS_ROW).getId(), employee.getId());
        Assertions.assertEquals(employeeService.parseEmployeeData(GenerateStubs.EMPLOYEE_AS_ROW).getProjectId(), employee.getProjectId());
    }
}
