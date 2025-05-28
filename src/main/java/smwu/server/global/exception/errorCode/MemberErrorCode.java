package smwu.server.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode{
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 유저가 존재하지 않습니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST.value(), "중복된 아이디입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
    PASSWORD_REUSED(HttpStatus.BAD_REQUEST.value(), "이전과 같은 비밀번호를 사용할 수 없습니다."),
    DEPENDENT_USER_ALREADY_LINKED(HttpStatus.BAD_REQUEST.value(), "이미 보호자와 연결된 피보호자입니다."),
    NO_MATCHING_USER(HttpStatus.BAD_REQUEST.value(), "일치하는 유저를 찾을 수 없습니다."),
    USER_IS_NOT_GUARDIAN(HttpStatus.BAD_REQUEST.value(), "보호자 계정이 아닙니다.")
    ;

    private final int statusCode;
    private final String message;
}
