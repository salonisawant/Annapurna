package com.annapurna.service;

import java.sql.SQLException;

import com.annapurna.dao.UserDAO;
import com.annapurna.model.User;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(String firstName, String lastName, String email, String password, String phone, String role) throws SQLException {
        // Check if user already exists
        if (userDAO.getUserByEmail(email) != null) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Create new user
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(password); // In a real application, you should hash the password
        newUser.setPhone(phone);
        newUser.setRole(role);

        userDAO.addUser(newUser);
        return newUser;
    }

    public User loginUser(String email, String password) throws SQLException {
        User user = userDAO.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) { // In a real application, you should compare hashed passwords
            return user;
        }
        return null;
    }

    
}