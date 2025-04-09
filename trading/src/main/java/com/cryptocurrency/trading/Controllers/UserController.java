package com.cryptocurrency.trading.Controllers;

import com.cryptocurrency.trading.Models.User;
import com.cryptocurrency.trading.Service.AccountResetService;
import com.cryptocurrency.trading.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccountResetService accountResetService;


    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) throws SQLException {
        userService.userValidator(user, bindingResult);

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> fieldError.getDefaultMessage(),
                            (existing, replacement) -> existing // Ако има повече от една грешка за дадено поле, вземи първата
                    ));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            userService.createUser(user, bindingResult);
            return ResponseEntity.ok(Map.of("message", "User is created successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("errors", "username:" + e.getMessage()));
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

    @GetMapping("/balance/{userId}")
    public ResponseEntity<?> getUserBalance(@PathVariable int userId) throws SQLException {
        BigDecimal balance = userService.getBalance(userId);
        return ResponseEntity.ok(balance);

    }
}
