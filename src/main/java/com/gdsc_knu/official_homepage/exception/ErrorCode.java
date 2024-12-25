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

    // User
    USER_NOT_FOUND(404,HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다","U404"),
    USER_DUPLICATED(409, HttpStatus.CONFLICT, "이미 가입된 사용자입니다.","U409"),

    // Auth
    INVALID_PERMISSION(401,HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.","U401"),
    JWT_EXPIRED(401, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.","ST401"),
    JWT_NOT_FOUND(401, HttpStatus.UNAUTHORIZED, "엑세스 토큰이 존재하지 않습니다.","AT404"),
    RT_NOT_FOUND(401, HttpStatus.UNAUTHORIZED, "리프레시 토큰이 저장되어 있지 않습니다.","RT404"),
    JWT_INVALID(401, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.","ST400"),
    RT_INCORRECT(401, HttpStatus.UNAUTHORIZED, "리프레시 토큰이 일치하지 않습니다.","RT400"),
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "접근 권한이 없습니다.","U403"),

    // Application
    APPLICATION_NOT_FOUND(404,HttpStatus.NOT_FOUND, "지원서를 찾을 수 없습니다","A404"),
    APPLICATION_DUPLICATED(409, HttpStatus.CONFLICT, "이미 최종 제출된 지원서 입니다.","A409"),
    INVALID_APPLICATION_STATE(400, HttpStatus.BAD_REQUEST,"지원서 상태가 유효하지 않습니다.","A400"),
    APPLICATION_DEADLINE_EXPIRED(400, HttpStatus.BAD_REQUEST,"지원 기간이 만료되었습니다.","A400"),
    CLASS_YEAR_NOT_FOUND(404, HttpStatus.NOT_FOUND,"존재하지 않는 기수입니다.","A404"),
    INVALID_CLASS_YEAR(400, HttpStatus.BAD_REQUEST,"기수 정보 설정이 잘못 되었습니다.","A400"),
    CLASS_YEAR_DUPLICATED(409, HttpStatus.CONFLICT,"이미 존재하는 기수 이름입니다.","A409"),
    APPLICATION_FORBIDDEN(403, HttpStatus.FORBIDDEN, "지원서에 접근할 수 있는 권한이 없습니다.","A403"),
    CONCURRENT_FAILED(409, HttpStatus.CONFLICT, "다른 사용자가 수정중입니다.","AN409"),
    // Team


    INVALID_INPUT(400, HttpStatus.BAD_REQUEST,"잘못된 요청입니다.","T400"),

    // Post
    POST_NOT_FOUND(404, HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.","P404"),
    POST_FORBIDDEN(403, HttpStatus.FORBIDDEN, "게시글을 수정할 수 있는 권한이 없습니다.","P403"),
    POST_ALREADY_LIKED(409, HttpStatus.CONFLICT, "이미 좋아요를 누른 게시글입니다.","PL409"),
    POST_NOT_LIKED(404, HttpStatus.NOT_FOUND, "좋아요를 누르지 않은 게시글입니다.","PL404"),

    // Comment
    COMMENT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.","C404"),
    COMMENT_FORBIDDEN(403, HttpStatus.FORBIDDEN, "댓글을 수정할 수 있는 권한이 없습니다.","C403"),
    INVALID_COMMENT(400, HttpStatus.BAD_REQUEST, "상위 댓글에만 답글을 남길 수 있습니다.","C400"),

    // Server error
    FAILED_UPLOAD(500, HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.","F500"),
    FAILED_SEND_MAIL(500, HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송에 실패했습니다.","M500");

    private final int status;
    private final HttpStatus error;
    private final String message;
    private final String code;
}
