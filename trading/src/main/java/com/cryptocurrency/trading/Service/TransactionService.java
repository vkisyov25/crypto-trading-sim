package com.cryptocurrency.trading.Service;

import com.cryptocurrency.trading.DAO.TransactionDao;
import com.cryptocurrency.trading.Dtos.BuySummaryDto;
import com.cryptocurrency.trading.Models.Transaction;
import com.cryptocurrency.trading.Models.UserAsset;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionDao transactionDao;
    private final UserService userService;
    private final UserAssetService userAssetService;

    @Transactional
    public void buyCrypto(Transaction transaction) throws Exception {
        //Изчисляваме цената на покупката
        BigDecimal price = new BigDecimal(transaction.getPrice().toString());
        BigDecimal quantity = new BigDecimal(transaction.getQuantity().toString());
        BigDecimal minusMoney = price.multiply(quantity);

        //Проверяваме дали има достатъчно пари и ако да изваждаме от неговия баланс сумата на покупката
        boolean isUpdated = userService.hasEnoughAndSubtractBalance(transaction.getUserId(), minusMoney);
        if (isUpdated) {
            //Проверява дали потребителя има такава криптовалута купена - ако има увеличава нейното количество, ако пък няма
            //създава нов запис в базата данни
            if (userAssetService.userAssetExists(transaction.getUserId(), transaction.getSymbol())) {
                userAssetService.updateUserAssetQuantity(transaction.getUserId(), transaction.getSymbol(), transaction.getQuantity());
            } else {
                UserAsset userAsset = new UserAsset();
                userAsset.setSymbol(transaction.getSymbol());
                userAsset.setQuantity(transaction.getQuantity());
                userAsset.setUserId(transaction.getUserId());
                userAssetService.insertUserAsset(userAsset);
            }

        } else {
            throw new Exception("You don't have enough money");
        }

        //Записва транзакцията в базата данни
        transaction.setLocalDateTime(new Date());
        transactionDao.save(transaction);


    }

    @Transactional
    public void sellCrypto(Transaction transaction) throws Exception {
        BigDecimal currentSellPrice = transaction.getPrice();
        BigDecimal currentQuantity = transaction.getQuantity();
        int userId = transaction.getUserId();
        String symbol = transaction.getSymbol();

        // Взима количеството от базата данни и проверка дали потребителят има нужното количество
        BigDecimal cryptoQuantity = userAssetService.getCryptoQuantity(userId, symbol);
        if (transaction.getQuantity().compareTo(cryptoQuantity) > 0) {
            throw new Exception("You don't have enough quantity");
        }

       /* avgPrice = totalCost / totalQuantity
        profit = (sellPrice - avgPrice) * soldQuantity*/

        // Изчисляване на печалбата/загубата
        BuySummaryDto buySummaryDto = transactionDao.calculateBuySummary(userId, symbol);
        BigDecimal avgPrice = BigDecimal.valueOf(0);
        if (buySummaryDto.getTotalQuantity().compareTo(BigDecimal.ZERO) > 0) {
            avgPrice = buySummaryDto.getTotalPrice().divide(buySummaryDto.getTotalQuantity(), 2, RoundingMode.HALF_UP);
        } else {
            throw new Exception("You don't have " + transaction.getSymbol() + " crypto");
        }

        // Актуализиране на активите
        userAssetService.reduceQuantityBySymbol(userId, symbol, currentQuantity);
        BigDecimal cryptoQuantityInDb = userAssetService.getCryptoQuantity(userId, symbol);
        if (cryptoQuantityInDb.compareTo(BigDecimal.ZERO) == 0) {
            userAssetService.remove(userId, symbol);
        }

        // Записване на транзакцията
        BigDecimal priceDiff = currentSellPrice.subtract(avgPrice);
        BigDecimal profitOrLoss = priceDiff.multiply(currentQuantity);
        transaction.setProfitLoss(profitOrLoss);
        transaction.setLocalDateTime(new Date());
        transactionDao.save(transaction);

        // Актуализиране на баланса
        BigDecimal balance = currentSellPrice.multiply(currentQuantity).add(profitOrLoss);
        userService.updateBalance(userId, balance);
    }
}
