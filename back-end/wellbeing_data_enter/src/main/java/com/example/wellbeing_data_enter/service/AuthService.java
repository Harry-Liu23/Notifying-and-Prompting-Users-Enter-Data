package com.example.wellbeing_data_enter.service;

import com.example.wellbeing_data_enter.model.AuthRequest;
import com.example.wellbeing_data_enter.model.AuthResponse;
import com.example.wellbeing_data_enter.entity.User;
import com.example.wellbeing_data_enter.dao.UserDao;
import com.example.wellbeing_data_enter.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private AuthService authService;

    public AuthResponse signIn(AuthRequest request) {
        Optional<User> userOptional = userDao.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = authService.authenticateUser(request);
            if (user.getPassword().equals(request.getPassword())) { // You should use password encryption here
                return new AuthResponse(true, "Sign-in successful",token);
            } else {
                return new AuthResponse(false, "Invalid password",null);
            }
        }
        return new AuthResponse(false, "User not found",null);
    }

    public String authenticateUser(AuthRequest request) {
        Optional<User> userOptional = userDao.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if the password matches
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // Generate JWT Token
                return jwtTokenProvider.createToken(user.getEmail());
            }
        }
        return null; // Authentication failed
    }
}
