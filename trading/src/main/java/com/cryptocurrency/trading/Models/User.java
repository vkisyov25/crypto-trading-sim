package com.cryptocurrency.trading.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {
    private int id;
    private String username;
    private BigDecimal balance;
}
