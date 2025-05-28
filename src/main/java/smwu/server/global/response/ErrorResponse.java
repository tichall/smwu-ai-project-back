package smwu.server.global.response;

import smwu.server.global.exception.errorCode.ErrorCode;

public class ErrorResponse<T> extends BasicResponse<T> {

    public ErrorResponse(ErrorCode errorCode) {
        super(false, errorCode.getStatusCode(), errorCode.getMessage(), null);
    }

    public ErrorResponse(ErrorCode errorCode, String message) {
        super(false, errorCode.getStatusCode(), message, null);
    }

    public ErrorResponse(ErrorCode errorCode, T data) {
        super(false, errorCode.getStatusCode(), errorCode.getMessage(), data);
    }
}
