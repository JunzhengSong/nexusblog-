package com.nexusblog.controller;

import com.nexusblog.common.ApiResult;
import com.nexusblog.dto.LoginRequest;
import com.nexusblog.dto.LoginResponse;
import com.nexusblog.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResult<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ApiResult.ok(response);
    }

    @PostMapping("/logout")
    public ApiResult<Void> logout() {
        return ApiResult.ok(null, "退出登录成功");
    }
}
