package com.budget.budgetTracker.controller;

import com.budget.budgetTracker.dto.UserResponseDTO;
import com.budget.budgetTracker.entity.User;
import com.budget.budgetTracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody User user) {
        return ResponseEntity.ok(userService.loginUser(user));
    }
}
