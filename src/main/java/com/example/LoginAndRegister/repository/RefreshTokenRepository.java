package com.example.LoginAndRegister.repository;

import com.example.LoginAndRegister.entity.RefreshToken;
import com.example.LoginAndRegister.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);

}
