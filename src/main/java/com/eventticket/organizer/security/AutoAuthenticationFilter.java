package com.eventticket.organizer.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AutoAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Create authorities with ADMIN and ORGANIZER roles
        List<SimpleGrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_ORGANIZER")
        );
        
        // Create authentication token with the authorities
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "auto-user", 
                null, 
                authorities
        );
        
        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
