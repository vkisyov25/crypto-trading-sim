package com.cryptocurrency.trading.Service;

import com.cryptocurrency.trading.Models.User;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface UserService {
    void createUser(User user) throws SQLException;

    User getUserByUsername(String username) throws SQLException;

    void updateBalance(int userId, BigDecimal newBalance) throws SQLException;

    boolean hasEnoughAndSubtractBalance(int userId, BigDecimal amount) throws SQLException;

    void resetBalance(int userId) throws Exception;

    BigDecimal getBalance(int userId) throws SQLException;
}
