package com.estibenjack.coffeepos.ingredient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

//    @GetMapping
//    public List<String> getAllIngredients(){
//        return List.of("Coffee Beans", "Oat Milk", "Sugar");
//    }

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService){
        this.ingredientService=ingredientService;
    }

    @GetMapping
    public List<IngredientResponse> getAllIngredients(){
        return ingredientService.getAllIngredients();
    }
}
