package com.example.wellbeing_data_enter.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class AuthRequest {
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }
}
