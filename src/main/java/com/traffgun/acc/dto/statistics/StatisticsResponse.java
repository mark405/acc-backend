package com.traffgun.acc.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class StatisticsResponse {
    private Set<MonthStatisticsResponse> statistics;
    private int year;
}
