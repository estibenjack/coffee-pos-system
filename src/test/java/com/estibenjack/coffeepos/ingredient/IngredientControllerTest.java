package com.estibenjack.coffeepos.ingredient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngredientController.class)
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientService ingredientService;

    private IngredientResponse coffeeBeans(BigDecimal stock) {
        return new IngredientResponse(
                1L, "Coffee Beans", MeasurementUnit.GRAM, stock
        );
    }

    @Test
    void shouldReturnAllIngredients() throws Exception {
        when(ingredientService.getAllIngredients())
                .thenReturn(List.of(coffeeBeans(BigDecimal.ZERO)));

        mockMvc.perform(get("/api/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Coffee Beans"));
    }

    @Test
    void shouldReturnSingleExistingIngredient() throws Exception {
        when(ingredientService.getSingleIngredient(1L))
                .thenReturn(coffeeBeans(BigDecimal.ZERO));

        mockMvc.perform(get("/api/ingredients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Coffee Beans"));
    }

    @Test
    void shouldReturn404ForNonExistentIngredient() throws Exception {
        when(ingredientService.getSingleIngredient(999L))
                .thenThrow(new ResponseStatusException(
                        NOT_FOUND, "Ingredient not found: 999"
                ));

        mockMvc.perform(get("/api/ingredients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn201WhenIngredientCreated() throws Exception {
        IngredientResponse response = new IngredientResponse(
                2L, "Almond Milk", MeasurementUnit.MILLILITRE,
                BigDecimal.ZERO
        );

        when(ingredientService.createIngredient(any(IngredientRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Almond Milk",
                                    "unit": "MILLILITRE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Almond Milk"))
                .andExpect(jsonPath("$.unit").value("MILLILITRE"))
                .andExpect(jsonPath("$.stockQuantity").value(0));
    }

    @Test
    void shouldReplenishIngredientStock() throws Exception {
        when(ingredientService.replenishIngredient(
                eq(1L), any(StockAdjustmentRequest.class)))
                .thenReturn(coffeeBeans(new BigDecimal("500")));

        mockMvc.perform(post("/api/ingredients/1/replenish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"quantity": 500}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity").value(500));
    }

    @Test
    void shouldConsumeIngredientStock() throws Exception {
        when(ingredientService.consumeIngredient(
                eq(1L), any(StockAdjustmentRequest.class)))
                .thenReturn(coffeeBeans(new BigDecimal("300")));

        mockMvc.perform(post("/api/ingredients/1/consume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"quantity": 200}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity").value(300));
    }

    @Test
    void shouldReturn404WhenReplenishingNonexistentIngredient()
            throws Exception {

        when(ingredientService.replenishIngredient(
                eq(999L), any(StockAdjustmentRequest.class)))
                .thenThrow(new ResponseStatusException(
                        NOT_FOUND, "Ingredient not found: 999"
                ));

        mockMvc.perform(post("/api/ingredients/999/replenish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"quantity": 500}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenConsumingNonexistentIngredient()
            throws Exception {

        when(ingredientService.consumeIngredient(
                eq(999L), any(StockAdjustmentRequest.class)))
                .thenThrow(new ResponseStatusException(
                        NOT_FOUND, "Ingredient not found: 999"
                ));

        mockMvc.perform(post("/api/ingredients/999/consume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"quantity": 100}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn409WhenConsumingInsufficientStock()
            throws Exception {

        when(ingredientService.consumeIngredient(
                eq(1L), any(StockAdjustmentRequest.class)))
                .thenThrow(new IllegalStateException(
                        "Insufficient stock for ingredient: Coffee Beans"
                ));

        mockMvc.perform(post("/api/ingredients/1/consume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"quantity": 400}
                                """))
                .andExpect(status().isConflict())
                .andExpect(content().string(
                        "Insufficient stock for ingredient: Coffee Beans"
                ));
    }

    @Test
    void shouldRejectNegativeReplenishmentQuantity() throws Exception {
        mockMvc.perform(post("/api/ingredients/1/replenish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"quantity": -100}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(ingredientService);
    }

    @Test
    void shouldReturn400WhenConsumingNegativeQuantity() throws Exception {
        mockMvc.perform(post("/api/ingredients/1/consume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"quantity": -100}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenIngredientNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "     ",
                                    "unit": "GRAM"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenStockQuantityIsMissing() throws Exception {
        mockMvc.perform(post("/api/ingredients/1/consume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenStockQuantityIsZero() throws Exception {
        mockMvc.perform(post("/api/ingredients/1/replenish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"quantity": 0}
                                """))
                .andExpect(status().isBadRequest());
    }
}