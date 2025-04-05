package com.cryptocurrency.trading.Models;

import com.cryptocurrency.trading.Models.Enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Transaction {
    private int id;
    private String symbol;
    private TransactionType type;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal profitLoss;
    private Date localDateTime;
    private int userId;
}
