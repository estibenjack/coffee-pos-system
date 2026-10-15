package com.estibenjack.coffeepos.ingredient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    private IngredientService service;

    @BeforeEach
    void setUp() {
        service = new IngredientService(ingredientRepository);
    }

    @Test
    void shouldReturnSingleExistingIngredient() {
        Ingredient ingredient = new Ingredient(
                "Coffee Beans",
                MeasurementUnit.GRAM
        );

        when(ingredientRepository.findById(1L))
                .thenReturn(Optional.of(ingredient));

        IngredientResponse result = service.getSingleIngredient(1L);

        assertEquals("Coffee Beans", result.name());
        assertEquals(MeasurementUnit.GRAM, result.unit());

        verify(ingredientRepository).findById(1L);
    }

    @Test
    void shouldReturnErrorForNonExistentIngredient() {
        when(ingredientRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getSingleIngredient(999L)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void shouldReplenishIngredientStock() {
        Ingredient ingredient = new Ingredient(
                "Coffee Beans",
                MeasurementUnit.GRAM
        );

        when(ingredientRepository.findById(1L))
                .thenReturn(Optional.of(ingredient));

        when(ingredientRepository.save(ingredient))
                .thenReturn(ingredient);

        StockAdjustmentRequest request =
                new StockAdjustmentRequest(new BigDecimal("500"));

        IngredientResponse result = service.replenishIngredient(1L, request);

        assertEquals(
                0,
                new BigDecimal("500").compareTo(result.stockQuantity())
        );

        verify(ingredientRepository).save(ingredient);
    }

    @Test
    void shouldRejectInsufficientStockWithoutSaving() {
        Ingredient ingredient = new Ingredient(
                "Coffee Beans",
                MeasurementUnit.GRAM
        );

        ingredient.replenishStock(new BigDecimal("300"));

        when(ingredientRepository.findById(1L))
                .thenReturn(Optional.of(ingredient));

        StockAdjustmentRequest request =
                new StockAdjustmentRequest(new BigDecimal("400"));

        assertThrows(
                IllegalStateException.class,
                () -> service.consumeIngredient(1L, request)
        );

        verify(ingredientRepository, never()).save(any(Ingredient.class));

        assertEquals(
                0,
                new BigDecimal("300").compareTo(ingredient.getStockQuantity())
        );
    }
}
