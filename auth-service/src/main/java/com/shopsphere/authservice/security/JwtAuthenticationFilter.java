package com.shopsphere.authservice.security;

import com.shopsphere.authservice.entity.User;
import com.shopsphere.authservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if(header==null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try{
            Claims claims = jwtService.parseToken(token);
            String email = claims.getSubject();

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                userRepository.findByEmailIgnoreCase(email)
                        .filter(User::isEnabled)
                        .ifPresent(user->{
                            var authorities = List.of(
                                    new SimpleGrantedAuthority("ROLE_"+user.getRole().name())
                            );

                            var authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);

                            SecurityContextHolder.getContext().setAuthentication(authentication);

                        });

            }
        }catch (JwtException | IllegalArgumentException ex){
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);

    }
}
