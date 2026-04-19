package com.example.LoginAndRegister.service;

import com.example.LoginAndRegister.dto.AuthResponse;
import com.example.LoginAndRegister.dto.LoginRequest;
import com.example.LoginAndRegister.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken);

}
