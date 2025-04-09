package com.cryptocurrency.trading.Service;

import com.cryptocurrency.trading.DAO.UserDao;
import com.cryptocurrency.trading.Models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;


    @Override
    public void createUser(User user, BindingResult bindingResult) throws SQLException {
        userValidator(user, bindingResult);
        userDao.create(user);
    }

    public void userValidator(User user, BindingResult bindingResult) throws SQLException {
        if (userDao.userExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Username already exists");
        }
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
