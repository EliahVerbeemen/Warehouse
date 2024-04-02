package be.kdg.warehouse.Managers;

import be.kdg.warehouse.Models.Ingredient;
import be.kdg.warehouse.Repositories.IngredientRepository;
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
