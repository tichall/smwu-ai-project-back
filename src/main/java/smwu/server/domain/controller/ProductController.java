package smwu.server.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import smwu.server.domain.dto.FinancialProductResponseDto;
import smwu.server.domain.dto.OpenAiResponseDto;
import smwu.server.domain.dto.UserInputRequestDto;
import smwu.server.domain.service.ProductService;
import smwu.server.global.response.BasicResponse;
import smwu.server.global.security.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/chats")
    public ResponseEntity<BasicResponse<OpenAiResponseDto>> recommend(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserInputRequestDto requestDto
    ) {
        OpenAiResponseDto responseDto = productService.recommend(userDetails.getUser().getId(), requestDto);
        return ResponseEntity.ok()
                .body(BasicResponse.of("모델 응답 생성 완료", responseDto));
    }
}
