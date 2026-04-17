package com.nexusblog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nexusblog.dto.LoginRequest;
import com.nexusblog.dto.LoginResponse;
import com.nexusblog.entity.User;
import com.nexusblog.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", loginRequest.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

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
