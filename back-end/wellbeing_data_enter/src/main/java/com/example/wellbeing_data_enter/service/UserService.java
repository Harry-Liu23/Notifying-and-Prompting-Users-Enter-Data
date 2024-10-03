package com.example.wellbeing_data_enter.service;

import com.example.wellbeing_data_enter.dao.UserDao;
import com.example.wellbeing_data_enter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User registerUser(User user) throws Exception {
        // Check if user already exists by email
        if (userDao.findByEmail(user.getEmail()) != null) {
            throw new Exception("User with this email already exists");
        }
        // Save the new user to the database
        return userDao.save(user);
    }
}
