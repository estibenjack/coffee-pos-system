package com.estibenjack.coffeepos;

import com.estibenjack.coffeepos.ingredient.Ingredient;
import com.estibenjack.coffeepos.ingredient.MeasurementUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
public class CoffeePosSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeePosSystemApplication.class, args);
	}

}
