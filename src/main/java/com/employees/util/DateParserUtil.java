package com.employees.util;

import com.employees.configuration.EmployeeConfiguration;
import com.employees.exception.DateParsingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author HowaydaGamal
 * @created 1/7/2022
 */

@Component
@AllArgsConstructor
public class DateParserUtil {

    private final EmployeeConfiguration employeeConfiguration;

    public LocalDate getAllAvailableDateFormats(String date) throws DateParsingException {

        LocalDate localDate = null;
        String[] dateFormats = employeeConfiguration.getDateFormats();
        for (String dateFormat : dateFormats)
            try {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
                localDate = LocalDate.parse(date, dateTimeFormatter);
                return localDate;
            } catch (DateTimeParseException ignored) {
            }
        throw new DateParsingException("Unable to parse format of this date " + date);
    }
}
