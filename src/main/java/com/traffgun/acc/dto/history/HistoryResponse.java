package com.traffgun.acc.dto.history;

import com.traffgun.acc.dto.user.UserResponse;
import com.traffgun.acc.model.history.HistoryBody;
import com.traffgun.acc.model.history.HistoryType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class HistoryResponse {
    private Long id;
    private HistoryBody body;
    private UserResponse user;
    private HistoryType type;
    private Instant date;
}