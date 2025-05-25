package ru.mirea.policy.service.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.mirea.auth.lib.dto.ErrorResponseDto;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        log.error("Exception was thrown", e);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<ErrorResponseDto> handleException(EntityNotFoundException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidStateException.class)
    private ResponseEntity<ErrorResponseDto> handleException(InvalidStateException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
