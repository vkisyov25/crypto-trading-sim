package com.cryptocurrency.trading.DAO;

import com.cryptocurrency.trading.Dtos.BuySummaryDto;
import com.cryptocurrency.trading.Models.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class TransactionDao {
    private final DataSource dataSource;

    public void save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (symbol,type,quantity, price, profit_loss, date, user_id) VALUES (?,?,?,?,?,?,?)";

        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, transaction.getSymbol());
            preparedStatement.setString(2, transaction.getType().toString());
            preparedStatement.setBigDecimal(3, transaction.getQuantity());
            preparedStatement.setBigDecimal(4, transaction.getPrice());
            preparedStatement.setBigDecimal(5, transaction.getProfitLoss());
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(transaction.getLocalDateTime().getTime()));
            preparedStatement.setInt(7, transaction.getUserId());

            preparedStatement.executeUpdate();

        }
    }

    public BuySummaryDto calculateBuySummary(int userId, String symbol) throws SQLException {
        String sql = "SELECT SUM(quantity) AS total_quantity, SUM(quantity * price) AS total_price FROM transactions WHERE user_id = ? AND symbol = ? AND type = 'BUY'";

        BuySummaryDto buySummaryDto = new BuySummaryDto();
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, symbol);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                buySummaryDto.setTotalQuantity(resultSet.getBigDecimal("total_quantity"));
                buySummaryDto.setTotalPrice(resultSet.getBigDecimal("total_price"));
            }
        }
        return buySummaryDto;
    }

    public int removeTransactionsByUserId(int userId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE user_id =?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            return preparedStatement.executeUpdate();
        }
    }
}
