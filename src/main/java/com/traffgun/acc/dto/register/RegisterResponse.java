package com.traffgun.acc.dto.register;

import com.traffgun.acc.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private Long id;
    private String username;
    private Role role;
}
