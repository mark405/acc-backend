package com.traffgun.acc.model.history;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationCreatedHistoryBody implements HistoryBody{
    @Override
    public HistoryBodyType getType() {
        return HistoryBodyType.OPERATION_CREATED;
    }

    private final String board;

    private final String category;

    private final String operationType;

    @JsonCreator
    public OperationCreatedHistoryBody(
            @JsonProperty("board") String board,
            @JsonProperty("category") String category,
            @JsonProperty("operationType") String operationType) {
        this.board = board;
        this.category = category;
        this.operationType = operationType;
    }
}
