package com.cryptocurrency.trading.Service;

import com.cryptocurrency.trading.DAO.TransactionDao;
import com.cryptocurrency.trading.DAO.UserAssetDao;
import com.cryptocurrency.trading.DAO.UserDao;
import com.cryptocurrency.trading.Models.Transaction;
import com.cryptocurrency.trading.Models.UserAsset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionDao transactionDao;
    private final UserDao userDao;
    private final UserAssetDao userAssetDao;

    public void buyCrypto(Transaction transaction) throws Exception {
        BigDecimal price = new BigDecimal(transaction.getPrice().toString());
        BigDecimal quantity = new BigDecimal(transaction.getQuantity().toString());
        BigDecimal minusMoney = price.multiply(quantity);
        boolean isUpdated = userDao.subtractionFromBalance(transaction.getUserId(), minusMoney);
        if (isUpdated) {
            UserAsset userAsset = new UserAsset();
            userAsset.setSymbol(transaction.getSymbol());
            userAsset.setQuantity(transaction.getQuantity());
            userAsset.setUserId(transaction.getUserId());
            if (userAssetDao.userAssetExists(transaction.getUserId(), transaction.getSymbol())) {
                userAssetDao.updateUserAssetQuantity(transaction.getUserId(), transaction.getSymbol(), transaction.getQuantity());
            } else {
                userAssetDao.insertUserAsset(userAsset);
            }

        } else {
            throw new Exception("You don't have enough money");
        }

        transaction.setLocalDateTime(new Date());
        transactionDao.save(transaction);


    }
}
