package com.hms.confi;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hms.entity.AppUser;
import com.hms.repository.AppUserRepository;
import com.hms.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final AppUserRepository userRepository;

    public JWTFilter(JWTService jwtService, AppUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7).trim();
        if (token.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Bearer token is missing");
            return;
        }

        try {
            String username = jwtService.getUsername(token);

            if (username == null || username.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                userRepository.findByUsername(username).ifPresent(user -> {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
                            );
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        } catch (JWTVerificationException | IllegalArgumentException ex) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
