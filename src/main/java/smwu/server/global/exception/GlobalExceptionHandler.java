package smwu.server.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import smwu.server.global.exception.errorCode.CommonErrorCode;
import smwu.server.global.exception.errorCode.ErrorCode;
import smwu.server.global.response.ErrorResponse;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j(topic = "예외 발생")
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse<String>> handleValidException(MethodArgumentNotValidException e) {

        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existingValue, newValue) -> existingValue
                ));

        ObjectMapper mapper = new ObjectMapper();
        String response = "";
        try {
            response = mapper.writeValueAsString(errors);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        ErrorResponse<String> errorMessage = new ErrorResponse<>(CommonErrorCode.BAD_REQUEST, response);

        return ResponseEntity.status(CommonErrorCode.BAD_REQUEST.getStatusCode())
                .body(errorMessage);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse<Void>> handleCustomException(CustomException e) {
        log.error("{} 예외 발생", e.getClass());

        return ResponseEntity.status(e.getErrorCode().getStatusCode())
                .body(new ErrorResponse<>(e.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<Void>> handleAll(Exception e) {
        log.error("예외 발생", e);
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(new ErrorResponse<>(errorCode));
    }
}