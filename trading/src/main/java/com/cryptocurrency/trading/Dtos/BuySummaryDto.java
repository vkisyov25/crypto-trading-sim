package com.cryptocurrency.trading.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BuySummaryDto {
    private BigDecimal totalQuantity;
    private BigDecimal totalPrice;
}
