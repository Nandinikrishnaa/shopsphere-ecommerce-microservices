package com.shopsphere.authservice.service;

import com.shopsphere.authservice.dto.AuthResponse;
import com.shopsphere.authservice.dto.LoginRequest;
import com.shopsphere.authservice.dto.RegisterRequest;
import com.shopsphere.authservice.dto.UserResponse;
import com.shopsphere.authservice.entity.Role;
import com.shopsphere.authservice.entity.User;
import com.shopsphere.authservice.repository.UserRepository;
import com.shopsphere.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest){
        String email = registerRequest.email().trim().toLowerCase(Locale.ROOT);

        if(userRepository.existsByEmailIgnoreCase(email)){
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();
        user.setName(registerRequest.name().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Role.CUSTOMER);
        user.setEnabled(true);

        User savedUser = userRepository.saveAndFlush(user);

        return createAuthResponse(savedUser);
    }

    @Transactional
    public AuthResponse login(LoginRequest request){
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(()->new BadCredentialsException("Invalid email or password"));

        if(!user.isEnabled() || !passwordEncoder.matches(request.password(), user.getPasswordHash())){
            throw new BadCredentialsException("Invalid email or password");
        }

        return createAuthResponse(user);
    }

    private AuthResponse createAuthResponse(User user) {
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());

        return new AuthResponse(
                jwtService.generateToken(user),
                "Bearer",
                jwtService.getExpirationSeconds(),
                userResponse);
    }
}
