package com.example.LoginAndRegister.service;

import com.example.LoginAndRegister.entity.User;

public interface RefreshTokenService {

    String createRefreshToken(User user);

    User validateRefreshToken(String token);

}
