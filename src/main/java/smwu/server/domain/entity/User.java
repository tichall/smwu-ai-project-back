package smwu.server.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import smwu.server.domain.enums.OAuthProvider;

@Document(collection = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;

    private String email;
    private String password;
    private String name;
    private OAuthProvider oAuthProvider;
    private UserRole userRole;
    private UserStatus userStatus;
}
