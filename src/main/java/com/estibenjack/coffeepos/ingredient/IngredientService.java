package com.estibenjack.coffeepos.ingredient;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public IngredientResponse createIngredient(IngredientRequest request) {
        Ingredient ingredient = new Ingredient(request.name(), request.unit());

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        return toResponse(savedIngredient);
    }

    public List<IngredientResponse> getAllIngredients() {
        return ingredientRepository.findAll()
                .stream()
                .map(ingredient -> toResponse(ingredient))
                .toList();
    }

    public IngredientResponse getSingleIngredient(Long id) {
        Ingredient ingredient = findIngredientOrThrow(id);

        return toResponse(ingredient);
    }

    public IngredientResponse replenishIngredient(Long id, StockAdjustmentRequest request) {
        Ingredient ingredient = findIngredientOrThrow(id);

        ingredient.replenishStock(request.quantity());
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        return toResponse(savedIngredient);
    }

    public IngredientResponse consumeIngredient(Long id, StockAdjustmentRequest request) {
        Ingredient ingredient = findIngredientOrThrow(id);

        ingredient.consumeStock(request.quantity());
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        return toResponse(savedIngredient);
    }

    private Ingredient findIngredientOrThrow(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ingredient not found: " + id
                ));
    }

    private IngredientResponse toResponse(Ingredient ingredient) {
        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getUnit(),
                ingredient.getStockQuantity()
        );
    }
}
