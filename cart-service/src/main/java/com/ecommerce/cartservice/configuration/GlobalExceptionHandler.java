package com.ecommerce.cartservice.configuration;

import com.ecommerce.cartservice.exceptions.NotFoundException;
import com.ecommerce.cartservice.exceptions.UnauthorizedException;
import com.ecommerce.cartservice.dtos.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorizedException(UnauthorizedException ex) {
        var message = new ErrorResponseDto();
        message.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponseDto>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex) {
        var message = new ErrorResponseDto();
        message.setMessage(ex.getMessage());
        return new ResponseEntity<ErrorResponseDto>(message, HttpStatus.NOT_FOUND);
    }

}

