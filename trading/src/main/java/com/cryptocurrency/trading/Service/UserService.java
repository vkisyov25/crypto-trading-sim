package com.cryptocurrency.trading.Service;

import com.cryptocurrency.trading.DAO.UserDao;
import com.cryptocurrency.trading.Models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final UserAssetService userAssetService;
    /*private final TransactionService transactionService;*/


    public void createUser(User user) throws SQLException {
        if (userDao.userExists(user.getUsername())) {
            throw new IllegalArgumentException("Потребител с това име вече съществува!");
        }
        userDao.create(user);
    }

    public User getUserByUsername(String username) throws SQLException {
        return userDao.findByUsername(username);
    }

    public void updateBalance(int userId, BigDecimal newBalance) throws SQLException {
        userDao.updateBalance(userId, newBalance);
    }

    public boolean hasEnoughAndSubtractBalance(int userId, BigDecimal amount) throws SQLException {
        return userDao.hasEnoughAndSubtractBalance(userId, amount);
    }

    public void resetBalance(int userId) throws Exception {
        userDao.resetBalance(userId);
    }

    public BigDecimal getBalance(int userId) throws SQLException {
        return userDao.findBalance(userId);
    }
}
