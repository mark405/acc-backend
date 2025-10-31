package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.board.BoardResponse;
import com.traffgun.acc.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardMapper {

    public BoardResponse toDto(Board board) {
        return new BoardResponse(
                board.getId(),
                board.getName(),
                board.getLevelType(),
                board.getOperationType()
        );
    }
}
