package com.cryptocurrency.trading.Controllers;

import com.cryptocurrency.trading.Models.Transaction;
import com.cryptocurrency.trading.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

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
}
