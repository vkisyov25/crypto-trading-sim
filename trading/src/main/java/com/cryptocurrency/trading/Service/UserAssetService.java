package com.cryptocurrency.trading.Service;

import com.cryptocurrency.trading.Models.UserAsset;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface UserAssetService {
    List<UserAsset> getAssetsByUserId(int userId) throws SQLException;

    boolean userAssetExists(int userId, String symbol) throws SQLException;

    void updateUserAssetQuantity(int userId, String symbol, BigDecimal quantityToAdd) throws SQLException;

    void insertUserAsset(UserAsset userAsset) throws SQLException;

    BigDecimal getCryptoQuantity(int userId, String symbol) throws SQLException;

    void reduceQuantityBySymbol(int userId, String symbol, BigDecimal quantityToRemove) throws SQLException;

    void remove(int userId, String symbol) throws SQLException;

    void removeUserAssetsByUserId(int userId) throws Exception;
}
