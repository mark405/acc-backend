package com.traffgun.acc.dto.register;

import com.traffgun.acc.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private Long id;
    private String username;
    private UserRole role;
    private Boolean totpEnabled;
}
