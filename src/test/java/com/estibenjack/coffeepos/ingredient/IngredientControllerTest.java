package com.estibenjack.coffeepos.ingredient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnSingleExistingIngredient() throws Exception {
        mockMvc.perform(get("/api/ingredients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Coffee Beans"));
    }

    @Test
    void shouldReturn404ForNonExistentIngredient() throws Exception {
        mockMvc.perform(get("/api/ingredients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn201WhenIngredientCreated() throws Exception {
        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "name": "Almond Milk",
                    "unit": "MILLILITRE"
                }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Almond Milk"))
                .andExpect(jsonPath("$.unit").value("MILLILITRE"))
                .andExpect(jsonPath("$.stockQuantity").value(0));
    }

    @Test
    void shouldReplenishIngredientStock() throws Exception {
        mockMvc.perform(post("/api/ingredients/1/replenish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantity": 500
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Coffee Beans"))
                .andExpect(jsonPath("$.unit").value("GRAM"))
                .andExpect(jsonPath("$.stockQuantity").isNumber());
    }

    @Test
    void shouldReturn404WhenReplenishingNonexistentIngredient() throws Exception {
        mockMvc.perform(post("/api/ingredients/999/replenish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantity": 500
                            }
                            """))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectNegativeReplenishmentQuantity() throws Exception {
        mockMvc.perform(post("/api/ingredients/1/replenish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantity": -100
                            }
                            """))
                .andExpect(status().isBadRequest());
    }
}
