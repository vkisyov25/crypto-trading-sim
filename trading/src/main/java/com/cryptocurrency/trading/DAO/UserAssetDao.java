package com.cryptocurrency.trading.DAO;

import com.cryptocurrency.trading.Models.UserAsset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class UserAssetDao {
    private final DataSource dataSource;

    public boolean userAssetExists(int userId, String symbol) throws SQLException {
        String sql = "SELECT 1 FROM user_assets WHERE user_id = ? AND symbol = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, symbol);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void updateUserAssetQuantity(int userId, String symbol, BigDecimal quantityToAdd) throws SQLException {
        String sql = "UPDATE user_assets SET quantity = quantity + ? WHERE user_id = ? AND symbol = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, quantityToAdd);
            ps.setInt(2, userId);
            ps.setString(3, symbol);
            ps.executeUpdate();
        }
    }

    public void insertUserAsset(UserAsset userAsset) throws SQLException {
        String sql = "INSERT INTO user_assets (user_id, symbol, quantity) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userAsset.getUserId());
            ps.setString(2, userAsset.getSymbol());
            ps.setBigDecimal(3, userAsset.getQuantity());
            ps.executeUpdate();
        }
    }

    public List<UserAsset> findByUserId(int user_id) throws SQLException {
        String sql = "SELECT id, symbol,quantity, user_id FROM user_assets WHERE user_id =?";
        List<UserAsset> userAssetList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserAsset userAsset = new UserAsset();
                userAsset.setId(resultSet.getInt("id"));
                userAsset.setSymbol(resultSet.getString("symbol"));
                userAsset.setQuantity(resultSet.getBigDecimal("quantity"));
                userAsset.setUserId(resultSet.getInt("user_id"));
                userAssetList.add(userAsset);
            }
        }

        return userAssetList;
    }

    public BigDecimal findCryptoQuantity(int userId, String symbol) throws SQLException {
        String sql = "SELECT quantity FROM user_assets WHERE user_id =? AND symbol =?";

        BigDecimal quantity = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, symbol);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                quantity = resultSet.getBigDecimal("quantity");
            }
        }
        return quantity;
    }

    public void reduceQuantityBySymbol(int userId, String symbol, BigDecimal quantityToRemove) throws SQLException {
        String sql = "UPDATE user_assets SET quantity = quantity - ? WHERE user_id = ? AND symbol = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setBigDecimal(1, quantityToRemove);
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, symbol);
            preparedStatement.executeUpdate();

        }
    }


    public void remove(int userId, String symbol) throws SQLException {
        String sql = "DELETE FROM user_assets WHERE user_id = ? AND symbol = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, symbol);
            ps.executeUpdate();
        }
    }


}
