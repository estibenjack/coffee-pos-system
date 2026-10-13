package com.estibenjack.coffeepos.ingredient;

public record IngredientRequest(
        String name,
        MeasurementUnit unit
){
}
