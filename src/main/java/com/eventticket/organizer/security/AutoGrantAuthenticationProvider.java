package com.eventticket.organizer.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AutoGrantAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Grant ADMIN and ORGANIZER roles to all requests
        List<SimpleGrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_ORGANIZER")
        );
        
        return new UsernamePasswordAuthenticationToken(
                "auto-user", 
                null, 
                authorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
