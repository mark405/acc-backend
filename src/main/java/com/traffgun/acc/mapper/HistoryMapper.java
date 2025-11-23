package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.history.HistoryResponse;
import com.traffgun.acc.entity.History;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryMapper {
    private final UserMapper userMapper;

    public HistoryResponse toDto(History history) {
        return new HistoryResponse(
                history.getId(),
                history.getBody(),
                userMapper.toUserDto(history.getUser()),
                history.getType(),
                history.getDate()
        );
    }
}
