package com.estibenjack.coffeepos.ingredient;

import com.jayway.jsonpath.JsonPath;
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

    @Test
    void shouldConsumeIngredientStock() throws Exception {
        // declare and initialise new stock
        String response = mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Test Coffee Beans",
                                "unit": "GRAM"
                            }
                            """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Number idNumber = JsonPath.read(response, "$.id");
        long id = idNumber.longValue();

        // replenish by 500
        mockMvc.perform(post("/api/ingredients/{id}/replenish", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantity": 500
                            }
                            """))
                .andExpect(status().isOk());

        // consume 200
        mockMvc.perform(post("/api/ingredients/{id}/consume", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantity": 200
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Test Coffee Beans"))
                .andExpect(jsonPath("$.unit").value("GRAM"))
                .andExpect(jsonPath("$.stockQuantity").value(300));
    }

    @Test
    void shouldReturn400WhenConsumingInsufficientStock() throws Exception {
        mockMvc.perform(post("/api/ingredients/1/consume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantity": -1000
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenConsumingNegativeQuantity() throws Exception {
        mockMvc.perform(post("/api/ingredients/1/consume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantity": -100
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenConsumingNonexistentIngredient() throws Exception {
        mockMvc.perform(post("/api/ingredients/999/consume")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "quantity": 100
                            }
                            """))
                .andExpect(status().isNotFound());
    }
}
