package com.doctolib.childweight.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doctolib.childweight.domain.HealthcareProvider;
import com.doctolib.childweight.dto.LoginRequest;
import com.doctolib.childweight.dto.LoginResponse;
import com.doctolib.childweight.dto.RegisterRequest;
import com.doctolib.childweight.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        HealthcareProvider provider = authService.register(
            request.name(), request.facilityId(),
            request.professionalId(), request.password()
        );
        return ResponseEntity.ok(provider);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.professionalId(), request.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
