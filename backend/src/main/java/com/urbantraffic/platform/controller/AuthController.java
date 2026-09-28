package com.urbantraffic.platform.controller;

import com.urbantraffic.platform.dto.AuthRequest;
import com.urbantraffic.platform.dto.RegisterRequest;
import com.urbantraffic.platform.entity.Role;
import com.urbantraffic.platform.entity.UserEntity;
import com.urbantraffic.platform.exception.ApiException;
import com.urbantraffic.platform.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", "User already exists");
            return ResponseEntity.badRequest().body(body);
        }

        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Registration successful");
        body.put("email", user.getEmail());
        body.put("role", user.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody AuthRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("id", user.getId());
        body.put("name", user.getName());
        body.put("email", user.getEmail());
        body.put("role", user.getRole());
        return ResponseEntity.ok(body);
    }
}
