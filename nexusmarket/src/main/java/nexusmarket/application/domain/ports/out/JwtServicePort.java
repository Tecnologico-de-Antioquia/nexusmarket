package nexusmarket.application.domain.ports.out;

import nexusmarket.application.domain.models.User;

public interface JwtServicePort {

    String generateToken(User user);

    boolean validateToken(String token);

    String extractUsername(String token);

    Integer extractTokenVersion(String token);

    User reconstructUser(String token);

    long getExpirationMs();
}
