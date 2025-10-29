package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.register.RegisterRequest;
import com.traffgun.acc.dto.register.RegisterResponse;
import com.traffgun.acc.dto.user.UserResponse;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.model.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword()) // will be encoded in UserService
                .role(Role.USER)
                .build();
    }

    public RegisterResponse toDto(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

    public UserResponse toUserDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }
}
