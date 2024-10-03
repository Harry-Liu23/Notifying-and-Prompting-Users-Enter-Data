package com.example.wellbeing_data_enter.model;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token = null;

    public AuthResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

}
