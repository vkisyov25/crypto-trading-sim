package com.cryptocurrency.trading.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserAsset {
    private int id;
    private String symbol;
    private BigDecimal quantity;
    private int userId;
}
