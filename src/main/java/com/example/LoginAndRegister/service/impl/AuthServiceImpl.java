package com.example.LoginAndRegister.service.impl;

import com.example.LoginAndRegister.dto.AuthResponse;
import com.example.LoginAndRegister.dto.LoginRequest;
import com.example.LoginAndRegister.dto.RegisterRequest;
import com.example.LoginAndRegister.entity.User;
import com.example.LoginAndRegister.enums.Role;
import com.example.LoginAndRegister.repository.UserRepository;
import com.example.LoginAndRegister.security.JwtUtil;
import com.example.LoginAndRegister.service.AuthService;
import com.example.LoginAndRegister.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void register(RegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Password not match");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        log.info("User registered: {}", request.getUsername());
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateToken(user.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(user);

        log.info("User login success: {}", user.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {

        User user = refreshTokenService.validateRefreshToken(refreshToken);

        String newAccessToken = jwtUtil.generateToken(user.getUsername());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .build();
    }
}
