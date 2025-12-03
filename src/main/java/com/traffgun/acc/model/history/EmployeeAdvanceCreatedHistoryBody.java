package com.traffgun.acc.model.history;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeAdvanceCreatedHistoryBody implements HistoryBody{
    @Override
    public HistoryBodyType getType() {
        return HistoryBodyType.EMPLOYEE_ADVANCE_CREATED;
    }

    private final String employee;

    private final Instant date;

    @JsonCreator
    public EmployeeAdvanceCreatedHistoryBody(
            @JsonProperty("employee") String employee,
            @JsonProperty("date") Instant date) {
        this.employee = employee;
        this.date = date;
    }
}
