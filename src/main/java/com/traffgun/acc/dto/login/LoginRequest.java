package com.traffgun.acc.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "Login request payload")
@Data
public class LoginRequest {
    @Schema(description = "Username of the user", example = "mark")
    @NotBlank
    private String username;

    @Schema(description = "User password", example = "password123")
    @NotBlank
    private String password;
}