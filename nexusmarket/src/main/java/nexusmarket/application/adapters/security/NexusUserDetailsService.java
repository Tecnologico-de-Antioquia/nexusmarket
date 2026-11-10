package nexusmarket.application.adapters.security;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NexusUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepositoryPort.findByUsername(username)
                .map(AuthenticatedUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado."));
    }
}
