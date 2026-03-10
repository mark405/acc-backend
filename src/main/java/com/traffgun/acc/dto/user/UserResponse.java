package com.traffgun.acc.dto.user;

import com.traffgun.acc.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private UserRole role;
    private Instant createdAt;
    private Instant modifiedAt;
    private Boolean totpEnabled;
}
