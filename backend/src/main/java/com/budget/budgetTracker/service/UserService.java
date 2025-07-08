package com.budget.budgetTracker.service;

import com.budget.budgetTracker.dto.UserResponseDTO;
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

        System.out.println("Email: " + user.getEmail() + " " + "name" + user.getName());
        if ((user.getEmail() != null && !user.getEmail().equalsIgnoreCase("")) &&
                (user.getName() != null && !user.getName().equalsIgnoreCase("")))
        {

            // Set creation time
            user.setCreatedAt(currentTimestamp());

            // Optional: trim name and email to avoid whitespace weirdness
            user.setName(user.getName() != null ? user.getName().trim() : "Unnamed User");
            user.setEmail(user.getEmail().trim());
            user.setTotalBalance(user.getTotalBalance());
            return userRepo.save(user);
        }
        else
        {
            throw new RuntimeException("No Email or Username provided");
        }
    }

    private String currentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    public UserResponseDTO loginUser(User user) {
        User existingUser = userRepo.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email. Please register first."));
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(existingUser.getId());
        dto.setName(existingUser.getName());
        dto.setEmail(existingUser.getEmail());
        dto.setCreatedAt(existingUser.getCreatedAt());
        return dto;
    }

}
