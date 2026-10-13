package com.estibenjack.coffeepos.ingredient;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class IngredientService {

    // temp in-memory storage while doing learning
    // will replace w/ spring data jpa repository when persistence introduced
    private final Map<Long, Ingredient> ingredients = new LinkedHashMap<>();
    private long nextId = 1L;

    public IngredientService() {
        addIngredient(new Ingredient("Coffee Beans", MeasurementUnit.GRAM));
        addIngredient(new Ingredient("Oat Milk", MeasurementUnit.MILLILITRE));
        addIngredient(new Ingredient("Sugar", MeasurementUnit.GRAM));
    }

    private void addIngredient(Ingredient ingredient){
        ingredients.put(nextId++, ingredient);
    }

    public IngredientResponse createIngredient(IngredientRequest request) {
        Ingredient ingredient = new Ingredient(request.name(), request.unit());

        long id = nextId;
        addIngredient(ingredient);

        return new IngredientResponse(
                id,
                ingredient.getName(),
                ingredient.getUnit(),
                ingredient.getStockQuantity()
        );
    }

    public List<IngredientResponse> getAllIngredients() {
        return ingredients.entrySet()
                .stream()
                .map(entry -> {
                    Ingredient ingredient = entry.getValue();
                    return new IngredientResponse(
                            entry.getKey(),
                            ingredient.getName(),
                            ingredient.getUnit(),
                            ingredient.getStockQuantity()
                    );
                })
                .toList();
    }

    public IngredientResponse getSingleIngredient(Long id) {
        Ingredient ingredient = ingredients.get(id);

        if (ingredient == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Ingredient not found: " + id
            );
        }

        return new IngredientResponse(
                id,
                ingredient.getName(),
                ingredient.getUnit(),
                ingredient.getStockQuantity()
        );
    }
}
