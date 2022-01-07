package com.employees.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author HowaydaGamal
 * @created 1/6/2022
 */

@Data
@AllArgsConstructor
@Component
public class EmployeeAsTeam {

    public EmployeeAsTeam() {
        this.numberOfDaysAsTeam = 0l;
    }

    private Employee firstEmployee;
    private Employee secondEmployee;
    private long numberOfDaysAsTeam;
}
