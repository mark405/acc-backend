package com.traffgun.acc.service;

import com.traffgun.acc.dto.statistics.MonthStatisticsResponse;
import com.traffgun.acc.dto.statistics.StatisticsResponse;
import com.traffgun.acc.entity.Board;
import com.traffgun.acc.entity.Operation;
import com.traffgun.acc.model.OperationType;
import com.traffgun.acc.repository.BoardRepository;
import com.traffgun.acc.repository.OperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final OperationRepository operationRepository;
    private final BoardService boardService;

    @Transactional(readOnly = true)
    public StatisticsResponse getStats(int year, OperationType type) {
        Instant start = Instant.parse(year + "-01-01T00:00:00Z");
        Instant end = Instant.parse(year + "-12-31T23:59:59Z");

        Board main = boardService.findMainBoard(type);
        Set<Operation> operations = operationRepository.findAllByDateBetweenAndBoardAndOperationType(start, end, main, type);

        ZoneId zone = ZoneId.of("Europe/Kyiv");

        Map<Integer, Double> monthTotals = operations.stream()
                .collect(Collectors.groupingBy(
                        op -> op.getDate().atZone(zone).getMonthValue(),
                        Collectors.summingDouble(Operation::getAmount)
                ));

        Set<MonthStatisticsResponse> stats = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> new MonthStatisticsResponse(
                        month,
                        monthTotals.getOrDefault(month, 0.0)
                ))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new StatisticsResponse(stats, year);
    }
}
