package smwu.server.global.jwt;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "refresh_tokens")
@Getter
@Builder
public class RefreshToken {
    @Id
    String id;
    private String email;
    private String token;

    // index를 사용할 필드 명시
    @Indexed(expireAfter = "PT168H")
    private Instant createdAt;
}
