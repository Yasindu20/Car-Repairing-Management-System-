package com.example.computer_item_repair.web;

import com.example.computer_item_repair.domain.User;
import com.example.computer_item_repair.payload.JWTLoginSucessReponse;
import com.example.computer_item_repair.payload.LoginRequest;
import com.example.computer_item_repair.security.JwtTokenProvider;
import com.example.computer_item_repair.services.MapValidationErrorService;
import com.example.computer_item_repair.services.UserService;
import com.example.computer_item_repair.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import static com.example.computer_item_repair.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
        logger.info("Login attempt for user: {}", loginRequest.getUsername());
        
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap != null) {
            logger.warn("Login validation errors: {}", result.getAllErrors());
            return errorMap;
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);
            
            logger.info("Login successful for user: {}", loginRequest.getUsername());
            return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));
        } catch (Exception e) {
            logger.error("Authentication error: ", e);
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
        logger.info("Registration attempt for user: {}", user.getUsername());
        
        // Validate passwords match
        userValidator.validate(user, result);

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap != null) {
            logger.warn("Registration validation errors: {}", result.getAllErrors());
            return errorMap;
        }

        try {
            User newUser = userService.saveUser(user);
            logger.info("User registered successfully: {}", newUser.getUsername());
            return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Registration error: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}