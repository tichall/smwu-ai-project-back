package smwu.server.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode {
    USER_FORBIDDEN(HttpStatus.FORBIDDEN.value(), "해당 기능에 대한 권한이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "회원을 찾을 수 없습니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST.value(), "로그인에 실패했습니다."),
    WITHDRAWAL_USER(HttpStatus.BAD_REQUEST.value(), "탈퇴한 회원입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST.value(), "해당 토큰으로 로그인이 불가능합니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다.")
    ;

    private final int statusCode;
    private final String message;
}

