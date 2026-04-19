package com.example.LoginAndRegister.service.impl;


import com.example.LoginAndRegister.entity.RefreshToken;
import com.example.LoginAndRegister.entity.User;
import com.example.LoginAndRegister.repository.RefreshTokenRepository;
import com.example.LoginAndRegister.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusSeconds(604800)); // 7 days

        refreshTokenRepository.save(token);

        return token.getToken();
    }

    @Override
    public User validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken.getUser();
    }
}
