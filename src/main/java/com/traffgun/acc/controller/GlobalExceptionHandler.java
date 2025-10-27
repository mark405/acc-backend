package com.traffgun.acc.controller;

import com.traffgun.acc.dto.error.ErrorResponse;
import com.traffgun.acc.exception.InvalidAccessTokenException;
import com.traffgun.acc.exception.InvalidRefreshTokenException;
import com.traffgun.acc.exception.InvalidUsernameOrPasswordException;
import com.traffgun.acc.exception.UserAlreadyExistsException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ErrorResponse handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler(InvalidAccessTokenException.class)
    public ErrorResponse handleInvalidAccessTokenException(InvalidAccessTokenException ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ErrorResponse handleInvalidUsernameOrPasswordException(InvalidUsernameOrPasswordException ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneralException(Exception ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}
