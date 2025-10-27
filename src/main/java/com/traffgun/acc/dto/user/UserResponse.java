package com.traffgun.acc.dto.user;

import com.traffgun.acc.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private Role role;
}
