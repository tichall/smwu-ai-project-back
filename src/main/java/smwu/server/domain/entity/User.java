package smwu.server.domain.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import smwu.server.domain.enums.OAuthProvider;

@Container(containerName = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;

    @PartitionKey
    private String email;
    private String password;
    private String name;
    private OAuthProvider oAuthProvider;
    private UserRole userRole;
    private UserStatus userStatus;
}
