package com.example.wellbeing_data_enter.controller;

import com.example.wellbeing_data_enter.model.AuthRequest;
import com.example.wellbeing_data_enter.model.AuthResponse;
import com.example.wellbeing_data_enter.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody AuthRequest request) {
        String token = authService.authenticateUser(request);
        if (token != null) {
            return ResponseEntity.ok(new AuthResponse(true, "Sign-in successful", token));
        } else {
            return ResponseEntity.badRequest().body(new AuthResponse(false, "Invalid credentials", null));
        }
    }
}
