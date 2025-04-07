package com.cryptocurrency.trading.Service;

import com.cryptocurrency.trading.DAO.UserAssetDao;
import com.cryptocurrency.trading.Models.UserAsset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAssetService {


    private final UserAssetDao userAssetDao;

    public List<UserAsset> getAssetsByUserId(int userId) throws SQLException {
        return userAssetDao.findByUserId(userId);
    }


    public boolean userAssetExists(int userId, String symbol) throws SQLException {
        return userAssetDao.userAssetExists(userId, symbol);
    }

    public void updateUserAssetQuantity(int userId, String symbol, BigDecimal quantityToAdd) throws SQLException {
        userAssetDao.updateUserAssetQuantity(userId, symbol, quantityToAdd);
    }

    public void insertUserAsset(UserAsset userAsset) throws SQLException {
        userAssetDao.insertUserAsset(userAsset);
    }


    public BigDecimal getCryptoQuantity(int userId, String symbol) throws SQLException {
        return userAssetDao.findCryptoQuantity(userId, symbol);
    }

    public void reduceQuantityBySymbol(int userId, String symbol, BigDecimal quantityToRemove) throws SQLException {
        userAssetDao.reduceQuantityBySymbol(userId, symbol, quantityToRemove);
    }


    public void remove(int userId, String symbol) throws SQLException {
        userAssetDao.remove(userId, symbol);
    }

    public void removeUserAssetsByUserId(int userId) throws Exception {
        userAssetDao.removeUserAssetsByUserId(userId);
    }
}
