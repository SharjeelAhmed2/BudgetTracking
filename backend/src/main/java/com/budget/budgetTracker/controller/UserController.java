package com.budget.budgetTracker.controller;

import com.budget.budgetTracker.entity.User;
import com.budget.budgetTracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> signUp(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }
}
