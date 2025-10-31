package com.traffgun.acc.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateBoardRequest {
    @NotBlank
    private String name;
}
