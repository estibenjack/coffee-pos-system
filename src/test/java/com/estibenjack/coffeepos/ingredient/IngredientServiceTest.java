package com.estibenjack.coffeepos.ingredient;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IngredientServiceTest {

    @Test
    void shouldReturnSingleExistingIngredient() {
        IngredientService service = new IngredientService();

        IngredientResponse result = service.getSingleIngredient(1L);

        assertEquals("Coffee Beans", result.name());
        assertEquals(MeasurementUnit.GRAM, result.unit());
    }

    @Test
    void shouldReturnErrorForNonExistentIngredient() {
        IngredientService service = new IngredientService();

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getSingleIngredient(999L)
        );

        assertEquals(404, exception.getStatusCode().value());
    }
}
