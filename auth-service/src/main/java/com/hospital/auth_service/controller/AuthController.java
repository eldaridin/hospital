package com.hospital.auth_service.controller;


import com.hospital.auth_service.dto.LoginRequest;
import com.hospital.auth_service.dto.LoginResponse;
import com.hospital.auth_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/publico")
    public ResponseEntity<String> publico() {
        return ResponseEntity.ok("Auth Service activo");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}

