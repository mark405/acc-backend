package com.traffgun.acc.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
}
