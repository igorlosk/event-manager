package dev.sorokin.eventnotificator.security.jwt;

import dev.sorokin.eventnotificator.user.AuthUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;

    public JwtTokenFilter(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwtToken;
        try {
            jwtToken = authorizationHeader.substring(7);
        } catch (Exception e) {
            logger.error("Error while reading jwt", e);
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println(jwtTokenManager.getUserIdFromToken(jwtToken));
        System.out.println(jwtTokenManager.getLoginFromToken(jwtToken));
        System.out.println(jwtTokenManager.getUserRole(jwtToken));

        AuthUser authUser = new AuthUser(
                jwtTokenManager.getUserIdFromToken(jwtToken),
                jwtTokenManager.getLoginFromToken(jwtToken),
                jwtTokenManager.getUserRole(jwtToken)
        );

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authUser,
                null,
                List.of(new SimpleGrantedAuthority(authUser.role()))
        );

        SecurityContextHolder.getContext().setAuthentication(token);

        filterChain.doFilter(request, response);
    }
}
