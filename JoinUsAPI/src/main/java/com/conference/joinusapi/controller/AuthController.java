package com.conference.joinusapi.controller;

import com.conference.joinusapi.dto.LoginRequest;
import com.conference.joinusapi.model.User;
import com.conference.joinusapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body("Email not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid password");
        }
        var userInfo = new java.util.HashMap<String, Object>();
        userInfo.put("fullName", user.getFullName());
        userInfo.put("id", user.getId());

        return ResponseEntity.ok(userInfo);
    }
}
