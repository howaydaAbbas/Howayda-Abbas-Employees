package com.employees.model;

import lombok.Data;

/**
 * @author HowaydaGamal
 * @created 1/6/2022
 */

@Data
public class EmployeeAsTeam {

    private Employee firstEmployee;
    private Employee secondEmployee;
    private long numberOfDaysAsTeam;
}
