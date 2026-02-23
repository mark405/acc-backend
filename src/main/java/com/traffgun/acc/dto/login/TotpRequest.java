package com.traffgun.acc.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TotpRequest {
    @Schema(description = "Username of the user", example = "mark")
    @NotBlank
    private String username;

    @NotNull
    private Integer code;
}