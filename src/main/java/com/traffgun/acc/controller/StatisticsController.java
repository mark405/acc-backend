package com.traffgun.acc.controller;

import com.traffgun.acc.dto.statistics.StatisticsResponse;
import com.traffgun.acc.model.OperationType;
import com.traffgun.acc.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService service;

    @GetMapping()
    public StatisticsResponse getStats(@RequestParam("year") Integer year,
                                       @RequestParam("type") OperationType type) {
        return service.getStats(year, type);
    }
}
