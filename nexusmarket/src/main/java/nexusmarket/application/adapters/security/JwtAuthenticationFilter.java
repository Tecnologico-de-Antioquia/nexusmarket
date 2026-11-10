package nexusmarket.application.adapters.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.ports.out.JwtServicePort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtServicePort jwtServicePort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(BEARER_PREFIX)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = header.substring(BEARER_PREFIX.length());
            authenticate(token, request);
        }
        filterChain.doFilter(request, response);
    }

    private void authenticate(String token, HttpServletRequest request) {
        if (!jwtServicePort.validateToken(token)) {
            return;
        }
        String username = jwtServicePort.extractUsername(token);
        if (username == null) {
            return;
        }
        userRepositoryPort.findByUsername(username).ifPresent(user -> {
            if (isTokenVersionValid(token, user) && user.isActive()) {
                AuthenticatedUserPrincipal principal = new AuthenticatedUserPrincipal(user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        });
    }

    private boolean isTokenVersionValid(String token, User user) {
        Integer tokenVersion = jwtServicePort.extractTokenVersion(token);
        int storedVersion = user.getAuthTokenVersion() == null ? 1 : user.getAuthTokenVersion();
        return tokenVersion != null && tokenVersion == storedVersion;
    }
}
