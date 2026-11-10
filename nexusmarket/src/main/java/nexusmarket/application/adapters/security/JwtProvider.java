package nexusmarket.application.adapters.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.JwtServicePort;
import nexusmarket.application.domain.valueobjects.SystemRole;
import nexusmarket.application.domain.valueobjects.UserStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider implements JwtServicePort {

    private final SecretKey key;
    private final long expirationMs;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .subject(user.getUsername())
                .id(UUID.randomUUID().toString())
                .claim("uid", user.getId())
                .claim("role", user.getRole() == null ? null : user.getRole().getCode())
                .claim("state", user.getState() == null ? null : user.getState().getCode())
                .claim("ver", user.getAuthTokenVersion() == null ? 1 : user.getAuthTokenVersion())
                .claim("email", user.getEmail())
                .claim("name", user.getFullName())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    @Override
    public Integer extractTokenVersion(String token) {
        Object value = parseClaims(token).get("ver");
        return value instanceof Number number ? number.intValue() : null;
    }

    @Override
    public User reconstructUser(String token) {
        Claims claims = parseClaims(token);
        User user = new User();
        user.setId(claims.get("uid", String.class));
        user.setUsername(claims.getSubject());
        user.setEmail(claims.get("email", String.class));
        user.setFullName(claims.get("name", String.class));
        String role = claims.get("role", String.class);
        user.setRole(role == null ? null : SystemRole.fromCode(role));
        String state = claims.get("state", String.class);
        user.setState(state == null ? UserStatus.ACTIVE : UserStatus.fromCode(state));
        Object version = claims.get("ver");
        user.setAuthTokenVersion(version instanceof Number number ? number.intValue() : 1);
        return user;
    }

    @Override
    public long getExpirationMs() {
        return expirationMs;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
