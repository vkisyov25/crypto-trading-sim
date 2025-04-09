package com.cryptocurrency.trading.Service;

import com.cryptocurrency.trading.Models.Transaction;
import jakarta.transaction.Transactional;

import java.sql.SQLException;
import java.util.List;

public interface TransactionService {
    @Transactional
    void buyCrypto(Transaction transaction) throws Exception;

    @Transactional
    void sellCrypto(Transaction transaction) throws Exception;

    void removeTransactionsByUserId(int userId) throws Exception;

    List<Transaction> getTransactionsByUserId(int userId) throws SQLException;
}
