package com.estibenjack.coffeepos.ingredient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IngredientRequest(
        @NotBlank String name,
        @NotNull MeasurementUnit unit
){
}
