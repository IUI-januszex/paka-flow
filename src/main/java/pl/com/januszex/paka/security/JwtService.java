package pl.com.januszex.paka.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@ConfigurationProperties("services.security")
@Data
public class JwtService {
    private String jwtSecret;
    private String audience;
    private String issuer;

    public boolean isTokenValid(String authToken) {
        if (authToken == null || authToken.equals("")) {
            return false;
        }
        try {
            Jwts.parser()
                    .requireAudience(audience)
                    .requireIssuer(issuer)
                    .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException ignored) {
            return false;
        }
    }

    @SneakyThrows
    public UserData getAuthFromJWT(String token) {
        Claims claims = Jwts.parser()
                .requireIssuer(issuer)
                .requireAudience(audience)
                .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
        return UserData
                .builder()
                .id(claims.get("Id", String.class))
                .username(claims.get("UserName", String.class))
                .role(claims.get("Role", String.class))
                .build();
    }
}
