package com.traffgun.acc.mapper;

import com.traffgun.acc.dto.project.ProjectResponse;
import com.traffgun.acc.dto.register.RegisterRequest;
import com.traffgun.acc.dto.register.RegisterResponse;
import com.traffgun.acc.dto.user.UserResponse;
import com.traffgun.acc.entity.Project;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toEntity(RegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(UserRole.USER)
                .build();
    }

    public RegisterResponse toDto(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getTotpEnabled()
        );
    }

    public UserResponse toUserDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt(),
                user.getModifiedAt(),
                user.getTotpEnabled(),
                List.of(),
                user.getOffersEditable()
        );
    }

    public UserResponse toUserDto(User user, List<ProjectResponse> projects) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt(),
                user.getModifiedAt(),
                user.getTotpEnabled(),
               projects,
                user.getOffersEditable()
        );
    }
}
