package com.traffgun.acc.controller;

import com.traffgun.acc.dto.user.UserResponse;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.InvalidAccessTokenException;
import com.traffgun.acc.exception.UserNotFoundException;
import com.traffgun.acc.mapper.UserMapper;
import com.traffgun.acc.service.UserService;
import com.traffgun.acc.utils.CookieUtils;
import com.traffgun.acc.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    @GetMapping("/me")
    public UserResponse getCurrentUser(HttpServletRequest request) {
        String accessToken = CookieUtils.getToken(request, CookieUtils.ACCESS_TOKEN_COOKIE);

        if (accessToken == null || !jwtUtils.validateToken(accessToken, "access")) {
            throw new InvalidAccessTokenException();
        }

        String username = jwtUtils.extractUsername(accessToken);

        User user = userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        return userMapper.toUserDto(user);
    }
}
