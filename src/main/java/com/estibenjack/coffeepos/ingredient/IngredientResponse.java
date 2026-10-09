package com.estibenjack.coffeepos.ingredient;

import java.math.BigDecimal;

public record IngredientResponse(
        Long id,
        String name,
        MeasurementUnit unit,
        BigDecimal stockQuantity
){
}
