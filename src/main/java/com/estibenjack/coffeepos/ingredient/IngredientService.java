package com.estibenjack.coffeepos.ingredient;

import org.springframework.stereotype.Service;

//import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IngredientService {

    // temp in-memory storage while doing learning
    // will replace w/ spring data jpa repository when persistence introduced
    private final Map<Long, Ingredient> ingredients = new HashMap<>();
    private long nextId = 1L;

    public IngredientService() {
        addIngredient(new Ingredient("Coffee Beans", MeasurementUnit.GRAM));
        addIngredient(new Ingredient("Oat Milk", MeasurementUnit.MILLILITRE));
        addIngredient(new Ingredient("Sugar", MeasurementUnit.GRAM));
    }

    private void addIngredient(Ingredient ingredient){
        ingredients.put(nextId++, ingredient);
    }

//    public Collection<Ingredient> getAllIngredients(){
//        return ingredients.values();
//    }

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
                }).toList();
    }
}
