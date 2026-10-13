package com.estibenjack.coffeepos.ingredient;

import java.math.BigDecimal;

public record StockAdjustmentRequest(BigDecimal quantity) {
}
