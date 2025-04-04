package com.cryptocurrency.trading.DAO;

import com.cryptocurrency.trading.Models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final DataSource dataSource;

    public void create(User user) throws SQLException {
        String sql = "INSERT INTO users (username, balance) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setBigDecimal(2, user.getBalance());
            preparedStatement.executeUpdate();
        }
    }

    public boolean userExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean subtractionFromBalance(int userId, BigDecimal amount) throws SQLException {
        String sql = "UPDATE users SET balance = balance - ? WHERE id = ? AND balance >= ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setBigDecimal(1, amount);
            preparedStatement.setInt(2, userId);
            preparedStatement.setBigDecimal(3, amount);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated == 1;
        }
    }


    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getBigDecimal("balance")
                );
            }
        }
        return null;
    }

}