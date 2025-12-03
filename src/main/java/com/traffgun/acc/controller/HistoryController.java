package com.traffgun.acc.controller;

import com.traffgun.acc.dto.history.HistoryResponse;
import com.traffgun.acc.mapper.HistoryMapper;
import com.traffgun.acc.model.history.HistoryType;
import com.traffgun.acc.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;
    private final HistoryMapper historyMapper;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Page<HistoryResponse> getAllHistories(@RequestParam(required = false, value = "username") String username,
                                                 @RequestParam(required = false, value = "type") HistoryType type,
                                                 @RequestParam(required = false, value = "sort_by") String sortBy,
                                                 @RequestParam(required = false, value = "direction") String direction,
                                                 @RequestParam(required = false, value = "page") int page,
                                                 @RequestParam(required = false, value = "size") int size
    ) {
        return historyService.findAll(username, type, sortBy, direction, page, size).map(historyMapper::toDto);
    }
}
