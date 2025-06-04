package smwu.server.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ToggleLikeResponseDto {
    private String productId;
    private boolean liked;
}
