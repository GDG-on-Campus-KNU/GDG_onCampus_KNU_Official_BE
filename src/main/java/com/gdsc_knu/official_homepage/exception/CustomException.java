package com.gdsc_knu.official_homepage.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private int result;
    private ErrorCode errorCode;
    private String message;

    public CustomException(ErrorCode errorCode) {
        this.result = errorCode.getStatus();
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public CustomException(ErrorCode errorCode, String message) {
        this.result = errorCode.getStatus();
        this.errorCode = errorCode;
        this.message = message;
    }
}
