package com.cryptocurrency.trading.Controllers;

import com.cryptocurrency.trading.Models.User;
import com.cryptocurrency.trading.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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

}
