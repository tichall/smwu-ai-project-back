package smwu.server.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smwu.server.global.response.BasicResponse;
import smwu.server.global.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/v1/health-check")
public class HealthCheckController {
    @GetMapping
    public ResponseEntity<BasicResponse<String>> healthCheck(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String email = userDetails.getUser().getEmail();
        return ResponseEntity
                .ok()
                .body(BasicResponse.of("테스트",email));
    }
}
