package com.gdsc_knu.official_homepage.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 대문자로 ErrorCode명 정의 (상태코드 / HttpStatus / 메세지)
     */

    NOT_FOUND(404,HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    INVALID_PERMISSION(401,HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_INPUT(400, HttpStatus.BAD_REQUEST,"잘못된 요청입니다."),
    CONFLICT(409, HttpStatus.CONFLICT, "이미 최종 제출된 지원서 입니다."),
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    JWT_EXPIRED(401, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    JWT_NOT_FOUND(401, HttpStatus.UNAUTHORIZED, "엑세스 토큰이 존재하지 않습니다."),
    JWT_INVALID(401, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    JWT_INCORRECT(401, HttpStatus.UNAUTHORIZED, "리프레시 토큰이 일치하지 않습니다."),
    FAILED_UPLOAD(500, HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");

    private final int status;
    private final HttpStatus error;
    private final String message;
}
