package com.traffgun.acc.controller;

import com.traffgun.acc.dto.login.LoginRequest;
import com.traffgun.acc.dto.register.RegisterRequest;
import com.traffgun.acc.dto.register.RegisterResponse;
import com.traffgun.acc.entity.User;
import com.traffgun.acc.exception.InvalidRefreshTokenException;
import com.traffgun.acc.exception.InvalidUsernameOrPasswordException;
import com.traffgun.acc.exception.UserAlreadyExistsException;
import com.traffgun.acc.mapper.UserMapper;
import com.traffgun.acc.service.UserService;
import com.traffgun.acc.utils.CookieUtils;
import com.traffgun.acc.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT tokens as HttpOnly cookies")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        var userDetails = userService.loadUserByUsername(request.getUsername());
        if (!userService.checkPassword(request.getPassword(), userDetails.getPassword())) {
            throw new InvalidUsernameOrPasswordException();
        }

        String accessToken = jwtUtils.generateAccessToken(request.getUsername());
        String refreshToken = jwtUtils.generateRefreshToken(request.getUsername());

        CookieUtils.addCookie(response, CookieUtils.ACCESS_TOKEN_COOKIE, accessToken, (int) (jwtUtils.getAccessExpiration() / 1000), "/api");
        CookieUtils.addCookie(response, CookieUtils.REFRESH_TOKEN_COOKIE, refreshToken, (int) (jwtUtils.getRefreshExpiration() / 1000), "/api/auth/refresh");

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public RegisterResponse register(@RequestBody @Valid RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        User savedUser = userService.save(userMapper.toEntity(request));
        return userMapper.toDto(savedUser);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Use refresh token cookie to get a new access token")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtils.getToken(request, CookieUtils.REFRESH_TOKEN_COOKIE);

        if (refreshToken == null || !jwtUtils.validateToken(refreshToken, "refresh")) {
            throw new InvalidRefreshTokenException();
        }

        String username = jwtUtils.extractUsername(refreshToken);

        String newAccessToken = jwtUtils.generateAccessToken(username);

        CookieUtils.addCookie(response, CookieUtils.ACCESS_TOKEN_COOKIE, newAccessToken,
                (int) (jwtUtils.getAccessExpiration() / 1000), "/api");

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Clear access and refresh cookies and invalidate refresh token")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        CookieUtils.clearCookie(response, CookieUtils.ACCESS_TOKEN_COOKIE, "/api");
        CookieUtils.clearCookie(response, CookieUtils.REFRESH_TOKEN_COOKIE, "/api/auth/refresh");

        return ResponseEntity.noContent().build();
    }
}
