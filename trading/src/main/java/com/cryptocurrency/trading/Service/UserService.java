package com.cryptocurrency.trading.Service;

import com.cryptocurrency.trading.DAO.UserDao;
import com.cryptocurrency.trading.Models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(User user) throws SQLException {
        if (userDao.userExists(user.getUsername())) {
            throw new IllegalArgumentException("Потребител с това име вече съществува!");
        }
        userDao.create(user);
    }

    public User getUserByUsername(String username) throws SQLException {
        return userDao.findByUsername(username);
    }

}
