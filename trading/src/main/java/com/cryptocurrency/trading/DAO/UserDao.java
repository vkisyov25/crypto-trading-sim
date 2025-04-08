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

    public boolean hasEnoughAndSubtractBalance(int userId, BigDecimal amount) throws SQLException {
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

    public void updateBalance(int userId, BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setBigDecimal(1, newBalance);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();

        }
    }

    public int resetBalance(int userId) throws SQLException {
        String sql = "UPDATE users SET balance = 10000 WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);

            //връщаме броя на засегнатите редове
            return preparedStatement.executeUpdate();
        }
    }
    public BigDecimal findBalance(int userId) throws SQLException {
        String sql = "SELECT balance FROM users WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBigDecimal("balance");
            } else {
                throw new SQLException("User not found with ID: " + userId);
            }
        }
    }

}