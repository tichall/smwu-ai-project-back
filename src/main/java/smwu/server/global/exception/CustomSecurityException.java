package smwu.server.global.exception;


import smwu.server.global.exception.errorCode.SecurityErrorCode;

public class CustomSecurityException extends CustomException {
    public CustomSecurityException(SecurityErrorCode errorCode) {
        super(errorCode);
    }
}
