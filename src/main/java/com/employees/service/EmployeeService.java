package com.employees.service;

import com.employees.configuration.EmployeeConfiguration;
import com.employees.exception.DateParsingException;
import com.employees.model.Employee;
import com.employees.model.EmployeeAsTeam;
import com.employees.util.DateParserUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HowaydaGamal
 * @created 1/6/2022
 */

@Service
@AllArgsConstructor
public class EmployeeService {

    Map<String, List<Employee>> employeesPerProjectMap;

    EmployeeAsTeam employeeAsTeamWithLongestTime;

    private final EmployeeConfiguration employeeConfiguration;

    private final DateParserUtil dateParserUtil;

    public void parseEmployeesFile() throws IOException, DateParsingException {
        final String FILE_ACCESS_MODE = "r";
        RandomAccessFile file;
        file = new RandomAccessFile("src/main/resources/employees.txt", FILE_ACCESS_MODE);
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
        channel.read(buffer);
        buffer.flip();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < channel.size(); i++) {
            String temp = String.valueOf((char) buffer.get());
            if (!temp.equals("\n") && i != channel.size() - 1) {
                if (StringUtils.isNotBlank(temp))
                    result.append(temp);
            } else {
                if (i == channel.size() - 1)
                    result.append(temp);
                if (result.toString().contains(employeeConfiguration.getDateToHeader())) {
                    result = new StringBuilder();
                    continue;
                }
                Employee employee = parseEmployeeData(result.toString());
                setEmployeeAsTeamWithLongestTime(employee);
                result = new StringBuilder();
            }
        }
        channel.close();
        file.close();
        printResult(employeeAsTeamWithLongestTime);
    }

    public Employee parseEmployeeData(String result) throws DateParsingException {

        String[] employeeArray = result.split(",");
        return new Employee(employeeArray[0], employeeArray[1], dateParserUtil.getAllAvailableDateFormats(employeeArray[2])
                , employeeArray[3].equals(employeeConfiguration.getCurrentDateTo()) ? LocalDate.now() : dateParserUtil.getAllAvailableDateFormats(employeeArray[3]));
    }

    private void setEmployeeAsTeamWithLongestTime(Employee employee) {

        List<Employee> employeeList = new ArrayList<>();
        if (employeesPerProjectMap.get(employee.getProjectId()) != null) {
            employeeList = employeesPerProjectMap.get(employee.getProjectId());
            for (Employee secondEmployee : employeeList) {
                long numberOfDays = calculateNumberOfDaysAsTeam(employee, secondEmployee);
                if (numberOfDays != 0) {
                    if (numberOfDays > employeeAsTeamWithLongestTime.getNumberOfDaysAsTeam()) {
                        EmployeeAsTeam employeeAsTeam = new EmployeeAsTeam();
                        employeeAsTeam.setFirstEmployee(employee);
                        employeeAsTeam.setSecondEmployee(secondEmployee);
                        employeeAsTeam.setNumberOfDaysAsTeam(numberOfDays);
                        employeeAsTeamWithLongestTime = employeeAsTeam;
                    }
                }
            }
        }
        employeeList.add(employee);
        employeesPerProjectMap.put(employee.getProjectId(), employeeList);
    }

    public long calculateNumberOfDaysAsTeam(Employee firstEmployee, Employee secondEmployee) {
        LocalDate dateFrom = firstEmployee.getDateFrom();
        LocalDate dateTo = firstEmployee.getDateTo();
        long numberOfDays = 0L;
        if (secondEmployee.getDateFrom().isAfter(firstEmployee.getDateFrom()))
            dateFrom = secondEmployee.getDateFrom();
        if (secondEmployee.getDateTo().isBefore(firstEmployee.getDateTo()))
            dateTo = secondEmployee.getDateTo();
        if (dateFrom.isBefore(dateTo))
            numberOfDays = ChronoUnit.DAYS.between(dateFrom, dateTo);
        return numberOfDays;
    }

    private void printResult(EmployeeAsTeam employeeAsTeam) {

        System.out.println();
        System.out.println("First Employee ID, Second Employee ID, Project ID, Number of days as team");
        System.out.println(employeeAsTeam.getFirstEmployee().getId() + "\t\t\t\t\t" + employeeAsTeam.getSecondEmployee().getId() + "\t\t\t\t\t" +
                employeeAsTeam.getFirstEmployee().getProjectId() + "\t\t\t\t\t" + employeeAsTeam.getNumberOfDaysAsTeam());
        System.out.println();
    }

}
