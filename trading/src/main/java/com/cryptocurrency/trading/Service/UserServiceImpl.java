package com.cryptocurrency.trading.Service;

import com.cryptocurrency.trading.DAO.UserDao;
import com.cryptocurrency.trading.Models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;


    @Override
    public void createUser(User user) throws SQLException {
        if (userDao.userExists(user.getUsername())) {
            throw new IllegalArgumentException("Потребител с това име вече съществува!");
        }
        userDao.create(user);
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        return userDao.findByUsername(username);
    }

    @Override
    public void updateBalance(int userId, BigDecimal newBalance) throws SQLException {
        userDao.updateBalance(userId, newBalance);
    }

    @Override
    public boolean hasEnoughAndSubtractBalance(int userId, BigDecimal amount) throws SQLException {
        return userDao.hasEnoughAndSubtractBalance(userId, amount);
    }

    @Override
    public void resetBalance(int userId) throws Exception {
        userDao.resetBalance(userId);
    }

    @Override
    public BigDecimal getBalance(int userId) throws SQLException {
        return userDao.findBalance(userId);
    }
}
