package pl.com.januszex.paka.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserData {
    String id;
    String username;
    String role;

    public GrantedAuthority getAuthority() {
        return () -> "ROLE_" + role;
    }
}
