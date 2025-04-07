package com.cryptocurrency.trading.Controllers;

import com.cryptocurrency.trading.Models.Transaction;
import com.cryptocurrency.trading.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyCrypto(@RequestBody Transaction transaction) {
        try {
            transactionService.buyCrypto(transaction);
            return ResponseEntity.ok().body("Successful transaction");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/sell")
    public ResponseEntity<?> sellCrypto(@RequestBody Transaction transaction) {
        try {
            transactionService.sellCrypto(transaction);
            return ResponseEntity.ok("Successful transaction");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }

    @GetMapping("/history/by/user-id/{userId}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable int userId) throws SQLException {
        List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }
}
