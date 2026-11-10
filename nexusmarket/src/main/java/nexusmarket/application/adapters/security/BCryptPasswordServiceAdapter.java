package nexusmarket.application.adapters.security;

import nexusmarket.application.domain.ports.out.PasswordServicePort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordServiceAdapter implements PasswordServicePort {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordServiceAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encrypt(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
