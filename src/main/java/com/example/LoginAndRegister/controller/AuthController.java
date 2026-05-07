package com.example.LoginAndRegister.controller;

import com.example.LoginAndRegister.dto.AuthResponse;
import com.example.LoginAndRegister.dto.LoginRequest;
import com.example.LoginAndRegister.dto.LogoutRequest;
import com.example.LoginAndRegister.dto.RegisterRequest;
import com.example.LoginAndRegister.service.AuthService;
import com.example.LoginAndRegister.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request){
        refreshTokenService.logout(request);
        return ResponseEntity.ok("Logout Sukses");
    }

}
