package com.traffgun.acc.dto.board;

import com.traffgun.acc.model.OperationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBoardRequest {
    @NotBlank
    private String name;

    @NotNull
    private OperationType type;
}
