package smwu.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OpenAiResponseDto {
    private String response;

    public static OpenAiResponseDto of(String response) {
        return new OpenAiResponseDto(response);
    }
}
