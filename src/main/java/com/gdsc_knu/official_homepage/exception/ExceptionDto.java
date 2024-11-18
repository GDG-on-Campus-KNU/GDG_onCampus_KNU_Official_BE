package com.gdsc_knu.official_homepage.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionDto {
    private final int code;
    private final String message;
    private final HttpStatus data;

    public ExceptionDto(ErrorCode error) {
        this.code = error.getStatus();
        this.message = error.getCode();
        this.data = error.getError();
    }

    public ExceptionDto(ErrorCode error, String message) {
        this.code = error.getStatus();
        this.message = message;
        this.data = error.getError();
    }

    public ExceptionDto(HttpStatus httpStatus, String message) {
        this.code = httpStatus.value();
        this.message = message;
        this.data = httpStatus;
    }

}
