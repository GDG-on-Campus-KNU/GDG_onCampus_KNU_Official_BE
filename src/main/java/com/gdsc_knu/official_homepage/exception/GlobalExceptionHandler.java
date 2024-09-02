package com.gdsc_knu.official_homepage.exception;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final DiscordClient discordClient;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> ahiExceptionHandler(CustomException e, HttpServletRequest request) {
        e.printStackTrace();
        if (e.getErrorCode() != ErrorCode.JWT_EXPIRED)
            discordClient.sendErrorAlert(e, e.getMessage(), e.getErrorCode().getError(), request);
        log.error(e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getError())
                .body(new ExceptionDto(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionDto> jwtExceptionHandler(JwtException e, HttpServletRequest request) {
        log.error(e.getMessage());
        discordClient.sendErrorAlert(e, e.getMessage(), HttpStatus.UNAUTHORIZED, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionDto(ErrorCode.INVALID_PERMISSION, e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> illegalArgumentExceptionHandler(IllegalArgumentException e, HttpServletRequest request) {
        log.error(e.getMessage());
        discordClient.sendErrorAlert(e, e.getMessage(), HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(ErrorCode.INVALID_INPUT, e.getMessage()));
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDto> noSuchElementExceptionHandler(NoSuchElementException e, HttpServletRequest request) {
        log.error(e.getMessage());
        discordClient.sendErrorAlert(e, e.getMessage(), HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(ErrorCode.INVALID_INPUT, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDto> illegalStateException(IllegalStateException e, HttpServletRequest request) {
        log.error(e.getMessage());
        discordClient.sendErrorAlert(e, e.getMessage(), HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST,e.getMessage()));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDto> invalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e, HttpServletRequest request) {
        log.error(e.getMessage());
        discordClient.sendErrorAlert(e, e.getMessage(), HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST, "요청 데이터가 잘못되었습니다. 누락되거나, 올바른 타입인지 확인하세요."));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDto> sqlException(SQLException e, HttpServletRequest request) {
        log.error(e.getMessage());
        discordClient.sendErrorAlert(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDto> indexOutOfBoundsException(IndexOutOfBoundsException e, HttpServletRequest request) {
        log.error(e.getMessage());
        discordClient.sendErrorAlert(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDto> nullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error(e.getMessage());
        discordClient.sendErrorAlert(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDto> handlerMethodValidationException(HandlerMethodValidationException e, HttpServletRequest request) {
        log.error(e.getMessage());
        discordClient.sendErrorAlert(e, e.getMessage(), HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionDto(HttpStatus.BAD_REQUEST, "입력값이 잘못되었습니다.: " + e.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        log.error(errors.toString());
        discordClient.sendErrorAlert(e, errors.toString(), HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error(e.getMessage());
        discordClient.sendErrorAlert(e, e.getMessage(), HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
