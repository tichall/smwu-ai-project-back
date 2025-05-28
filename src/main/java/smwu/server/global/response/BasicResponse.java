package smwu.server.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasicResponse<T> {
    private boolean success;
    private int statusCode;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> BasicResponse<T> of() {
        return new BasicResponse<>(true, 200, "Success.", null);
    }

    public static <T> BasicResponse<T> of(int statusCode) {
        return new BasicResponse<>(true, statusCode, "Success.", null);
    }

    public static <T> BasicResponse<T> of(String message) {
        return new BasicResponse<>(true, 200, message, null);
    }

    public static <T> BasicResponse<T> of(int statusCode, String message) {
        return new BasicResponse<>(true, statusCode, message, null);
    }
    public static <T> BasicResponse<T> of(String message, T data) {
        return new BasicResponse<>(true, 200, message, data);
    }

    public static <T> BasicResponse<T> of(int statusCode, String message, T data) {
        return new BasicResponse<>(true, statusCode, message, data);
    }

    public static <T> BasicResponse<T> of(boolean success, int statusCode, String message, T data) {
        return new BasicResponse<>(success, statusCode, message, data);
    }
}
