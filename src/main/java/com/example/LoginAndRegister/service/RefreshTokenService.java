package com.example.LoginAndRegister.service;

import com.example.LoginAndRegister.dto.LogoutRequest;
import com.example.LoginAndRegister.entity.User;

public interface RefreshTokenService {

    String createRefreshToken(User user);

    User validateRefreshToken(String token);

    void logout (LogoutRequest request);
}
