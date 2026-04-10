package com.nexusblog.service;

import com.nexusblog.dto.LoginRequest;
import com.nexusblog.dto.LoginResponse;
import com.nexusblog.dto.mapper.DtoMapper;
import com.nexusblog.entity.User;
import com.nexusblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .message("Login successful")
                .build();
    }

    public boolean isAuthenticated() {
        // This would be used to check if current user is authenticated
        // Implementation depends on SecurityContext
        return true;
    }
}
