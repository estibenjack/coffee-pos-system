package com.estibenjack.coffeepos.ingredient;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StockAdjustmentRequest(
        @NotNull
        @DecimalMin(value="0.0", inclusive=false)
        BigDecimal quantity
) {
}
