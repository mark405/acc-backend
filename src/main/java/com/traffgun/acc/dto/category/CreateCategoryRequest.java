package com.traffgun.acc.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCategoryRequest {
    @NotNull
    private Long boardId;
    @NotBlank
    private String name;
    private String comment;
}
