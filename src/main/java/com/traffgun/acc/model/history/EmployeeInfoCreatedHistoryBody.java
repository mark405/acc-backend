package com.traffgun.acc.model.history;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeInfoCreatedHistoryBody implements HistoryBody{
    @Override
    public HistoryBodyType getType() {
        return HistoryBodyType.EMPLOYEE_INFO_CREATED;
    }

    private final String employee;

    private final LocalDate startDate;

    private final LocalDate endDate;

    @JsonCreator
    public EmployeeInfoCreatedHistoryBody(
            @JsonProperty("employee") String employee,
            @JsonProperty("startDate") LocalDate startDate,
            @JsonProperty("endDate") LocalDate endDate) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
