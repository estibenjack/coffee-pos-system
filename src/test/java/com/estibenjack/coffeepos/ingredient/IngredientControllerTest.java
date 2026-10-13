package com.estibenjack.coffeepos.ingredient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
