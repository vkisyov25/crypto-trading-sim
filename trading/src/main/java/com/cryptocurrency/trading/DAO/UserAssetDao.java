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

}
