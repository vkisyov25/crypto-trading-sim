package com.cryptocurrency.trading.DAO;

import com.cryptocurrency.trading.Dtos.BuySummaryDto;
import com.cryptocurrency.trading.Models.Enums.TransactionType;
import com.cryptocurrency.trading.Models.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Transaction> findTransactionsByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE user_id =?";
        List<Transaction> transactionList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setSymbol(resultSet.getString("symbol"));
                transaction.setQuantity(resultSet.getBigDecimal("quantity"));
                transaction.setPrice(resultSet.getBigDecimal("price"));
                transaction.setProfitLoss(resultSet.getBigDecimal("profit_loss"));
                transaction.setLocalDateTime(resultSet.getTimestamp("date"));
                transaction.setUserId(resultSet.getInt("user_id"));
                transaction.setType(TransactionType.valueOf(resultSet.getString("type")));
                transactionList.add(transaction);
            }
        }

        return transactionList;
    }
}
