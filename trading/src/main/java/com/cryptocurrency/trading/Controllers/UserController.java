package com.cryptocurrency.trading.Controllers;

import com.cryptocurrency.trading.Models.User;
import com.cryptocurrency.trading.Service.AccountResetService;
import com.cryptocurrency.trading.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccountResetService accountResetService;


    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            return ResponseEntity.ok("User is created successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Възникна грешка с базата данни");
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginProcess(@RequestParam String username) {
        try {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return ResponseEntity.status(404).body("There isn't user with this name");
            }
            return ResponseEntity.ok(user);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Error searching for user.");
        }
    }

    @GetMapping("/reset/by/id/{userId}")
    public ResponseEntity<?> resetUserAccount(@PathVariable int userId) {
        try {
            accountResetService.resetUserAccount(userId);
            return ResponseEntity.ok().body("User account is reset successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
