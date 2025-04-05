package com.cryptocurrency.trading.DAO;

import com.cryptocurrency.trading.Models.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class TransactionDao {
    private final DataSource dataSource;
    public void save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (symbol,type,quantity, price, profit_loss, date, user_id) VALUES (?,?,?,?,?,?,?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setString(1,transaction.getSymbol());
            preparedStatement.setString(2,transaction.getType().toString());
            preparedStatement.setBigDecimal(3,transaction.getQuantity());
            preparedStatement.setBigDecimal(4,transaction.getPrice());
            preparedStatement.setBigDecimal(5,transaction.getProfitLoss());
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(transaction.getLocalDateTime().getTime()));
            preparedStatement.setInt(7,transaction.getUserId());

            preparedStatement.executeUpdate();

        }
    }
}
