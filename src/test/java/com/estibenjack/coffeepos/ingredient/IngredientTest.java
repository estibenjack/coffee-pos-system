package com.estibenjack.coffeepos.ingredient;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientTest {

    @Test
    void replenishingStockShouldIncreaseQuantity(){
        Ingredient oatMilk = new Ingredient("Oat Milk", MeasurementUnit.MILLILITRE);
        oatMilk.replenishStock(new BigDecimal("1000"));
        assertEquals(0, new BigDecimal("1000").compareTo(oatMilk.getStockQuantity()));
    }

    @Test
    void consumingMoreThanAvailableShouldThrowException(){
        Ingredient oatMilk = new Ingredient("Oat Milk", MeasurementUnit.MILLILITRE);
        oatMilk.replenishStock(new BigDecimal("500"));
        assertThrows(IllegalStateException.class, () -> oatMilk.consumeStock(new BigDecimal("1000")));
    }

    @Test
    void consumingShouldDecreaseQuantity(){
        Ingredient oatMilk = new Ingredient("Oat Milk", MeasurementUnit.MILLILITRE);

        oatMilk.replenishStock(new BigDecimal("1000"));
        oatMilk.consumeStock(new BigDecimal("250"));

        assertEquals(0, new BigDecimal("750").compareTo(oatMilk.getStockQuantity()));
    }
}
