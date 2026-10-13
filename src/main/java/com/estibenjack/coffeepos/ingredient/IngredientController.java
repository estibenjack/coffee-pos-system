package com.estibenjack.coffeepos.ingredient;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService){
        this.ingredientService=ingredientService;
    }

    @GetMapping
    public List<IngredientResponse> getAllIngredients(){
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/{id}")
    public IngredientResponse getSingleIngredient(@PathVariable Long id){
        return ingredientService.getSingleIngredient(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientResponse createIngredient(@RequestBody IngredientRequest request) {
        return ingredientService.createIngredient(request);
    }
}
