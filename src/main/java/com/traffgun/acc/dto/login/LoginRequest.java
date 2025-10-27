package com.traffgun.acc.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Login request payload")
@Data
public class LoginRequest {
    @Schema(description = "Username of the user", example = "mark")
    private String username;

    @Schema(description = "User password", example = "password123")
    private String password;
}