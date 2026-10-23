package nexusmarket.application.infrastructure.security;

import org.springframework.stereotype.Service;

import nexusmarket.application.domain.ports.out.PasswordServicePort;

@Service
public class PlainPasswordService implements PasswordServicePort {

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return rawPassword != null && rawPassword.equals(encodedPassword);
    }

    @Override
    public String encrypt(String rawPassword) {
        return rawPassword;
    }
}
