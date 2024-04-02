package com.example.warehouse2.Managers;

import com.example.warehouse2.Models.Ingredient;
import com.example.warehouse2.Repositories.IngredientRepository;
import org.springframework.stereotype.Component;

@Component
public class IngredientManager implements IIngredientManager {
    private IngredientRepository repo;

    public IngredientManager(IngredientRepository repository){
        repo = repository;
    }

    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        return repo.save(ingredient);
    }
}
