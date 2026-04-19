package com.example.LoginAndRegister.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @GetMapping
    public ResponseEntity<?> getProfile(Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                Map.of(
                        "message", "Access granted",
                        "username", username
                )
        );
    }

}
