package com.example.computer_item_repair.services;

import com.example.computer_item_repair.domain.User;
import com.example.computer_item_repair.exceptions.UsernameAlreadyExistsException;
import com.example.computer_item_repair.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser) {
        try {
            logger.debug("Attempting to save user: {}", newUser.getUsername());
            
            // Check if username already exists
            User existingUser = userRepository.findByUsername(newUser.getUsername());
            if (existingUser != null) {
                logger.warn("Username already exists: {}", newUser.getUsername());
                throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");
            }
            
            // Encode password and save
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setConfirmPassword("");
            
            // Save to database
            User savedUser = userRepository.save(newUser);
            logger.info("User saved successfully: {}", savedUser.getUsername());
            
            return savedUser;
        } catch (UsernameAlreadyExistsException e) {
            logger.error("Username already exists: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error saving user: {}", e.getMessage(), e);
            throw new RuntimeException("Error occurred while registering user: " + e.getMessage());
        }
    }
}