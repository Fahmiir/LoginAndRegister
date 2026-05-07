package com.example.LoginAndRegister.service.impl;


import com.example.LoginAndRegister.dto.LogoutRequest;
import com.example.LoginAndRegister.entity.RefreshToken;
import com.example.LoginAndRegister.entity.User;
import com.example.LoginAndRegister.repository.RefreshTokenRepository;
import com.example.LoginAndRegister.repository.UserRepository;
import com.example.LoginAndRegister.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Override
    public String createRefreshToken(User user) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);
        String newToken = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusSeconds(86400);


        if(existingToken.isPresent()) {
            RefreshToken token = existingToken.get();
            token.setToken(newToken);
            token.setExpiryDate(expiry);
            refreshTokenRepository.save(token);
        } else {
            RefreshToken token = new RefreshToken();
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiryDate(LocalDateTime.now().plusSeconds(604800)); // 7 days

            refreshTokenRepository.save(token);
        }

        return newToken;
    }

    @Override
    public User validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken.getUser();
    }

    @Override
    public void logout(LogoutRequest request){
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User Not Found"));
        refreshTokenRepository.deleteByUser(user);
    }
}
