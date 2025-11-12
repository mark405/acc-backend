package com.traffgun.acc.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthStatisticsResponse {
    private int month;
    private Double amount;
}
