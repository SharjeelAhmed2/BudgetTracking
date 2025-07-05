package com.budget.budgetTracker.service;

import com.budget.budgetTracker.entity.User;
import com.budget.budgetTracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional
    public User createUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Set creation time
        user.setCreatedAt(currentTimestamp());

        // Optional: trim name and email to avoid whitespace weirdness
        user.setName(user.getName() != null ? user.getName().trim() : "Unnamed User");
        user.setEmail(user.getEmail().trim());

        return userRepo.save(user);
    }

    private String currentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
