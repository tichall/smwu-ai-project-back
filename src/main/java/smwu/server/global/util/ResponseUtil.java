package smwu.server.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import smwu.server.global.exception.errorCode.ErrorCode;
import smwu.server.global.response.BasicResponse;
import smwu.server.global.response.ErrorResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeJsonResponse(HttpServletResponse response, HttpStatus status, String message, Object data) throws IOException {
        BasicResponse<Object> responseMessage = BasicResponse.of(status.value(), message, data);

        String jsonResponse = objectMapper.writeValueAsString(responseMessage);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(jsonResponse);
    }

    /**
     * HttpResponse에 JSON 형태로 응답하기
     */
    public static void writeJsonErrorResponse(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {
        ErrorResponse responseMessage = new ErrorResponse(errorCode);

        String jsonResponse = objectMapper.writeValueAsString(responseMessage);

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE + "; charset=" + StandardCharsets.UTF_8.name());
        response.setStatus(errorCode.getStatusCode());
        response.getWriter().write(jsonResponse);
    }
}
