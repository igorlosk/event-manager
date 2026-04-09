package dev.sorokin.eventmanager.security.jwt;

import dev.sorokin.eventmanager.users.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.springframework.context.annotation.Lazy;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtTokenManager jwtTokenManager;

    private final UserService userService;

    public JwtTokenFilter(
            JwtTokenManager jwtTokenManager,
            @Lazy UserService userService) {
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
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

        String loginFromToken = jwtTokenManager.getLoginFromToken(jwtToken);

        User user = userService.findByLogin(loginFromToken);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority(user.role().toString()))
        );
        SecurityContextHolder.getContext().setAuthentication(token);

        filterChain.doFilter(request, response);

    }
}
