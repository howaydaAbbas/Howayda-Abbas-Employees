package com.employees.service;

import com.employees.configuration.EmployeeConfiguration;
import com.employees.model.Employee;
import com.employees.model.EmployeeAsTeam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HowaydaGamal
 * @created 1/6/2022
 */

@Service
@AllArgsConstructor
public class EmployeeService {

    Map<String, List<Employee>> employeesPerProjectMap = new HashMap<>();

    EmployeeAsTeam employeeAsTeamWithLongestTime;

    private final String FILE_ACCESS_MODE = "r";

    private final EmployeeConfiguration employeeConfiguration;

    public void parseEmployeesFile() {
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile("src/main/resources/employees.txt", FILE_ACCESS_MODE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileChannel channel = file.getChannel();
        try {
            ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
            channel.read(buffer);
            buffer.flip();
            String result = "";
            for (int i = 0; i < channel.size(); i++) {
                String temp = String.valueOf((char) buffer.get());
                if (!temp.equals("\n") && i != channel.size() - 1) {
                    if (StringUtils.isNotBlank(temp))
                        result += temp;
                } else {
                    if (i == channel.size() - 1)
                        result += temp;
                    if (result.contains(employeeConfiguration.getDateToHeader())) {
                        result = "";
                        continue;
                    }
                    Employee employee = parseEmployeeData(result);
                    setEmployeeAsTeamWithLongestTime(employee);
                    result = "";
                }
            }
            channel.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        printResult(employeeAsTeamWithLongestTime);
    }

    public Employee parseEmployeeData(String result) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String empArray[] = result.split(",");
        Employee employee = new Employee(empArray[0], empArray[1], LocalDate.parse(empArray[2], dateTimeFormatter)
                , empArray[3].equals(employeeConfiguration.getCurrentDateTo()) ? LocalDate.now() : LocalDate.parse(empArray[3], dateTimeFormatter));
        return employee;
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
        long numberOfDays = 0l;
        if (secondEmployee.getDateFrom().isAfter(firstEmployee.getDateFrom()))
            dateFrom = secondEmployee.getDateFrom();
        if (secondEmployee.getDateTo().isBefore(firstEmployee.getDateTo()))
            dateTo = secondEmployee.getDateTo();
        if (dateFrom.isBefore(dateTo))
            numberOfDays = ChronoUnit.DAYS.between(dateFrom, dateTo);
        return numberOfDays;
    }

    private void printResult(EmployeeAsTeam employeeAsTeam) {

        System.out.println(employeeAsTeam.getFirstEmployee().getId() + " " + employeeAsTeam.getSecondEmployee().getId() + " " +
                employeeAsTeam.getFirstEmployee().getProjectId() + " " + employeeAsTeam.getNumberOfDaysAsTeam());
    }

}
