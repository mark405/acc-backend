package com.traffgun.acc.dto.board;

import com.traffgun.acc.model.LevelType;
import com.traffgun.acc.model.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String name;
    private LevelType levelType;
    private OperationType operationType;

}
