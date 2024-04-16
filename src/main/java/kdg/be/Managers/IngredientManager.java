package kdg.be.Managers;

import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Repositories.IngredientRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    @Override
    public Optional<Ingredient> findIngredientById(Long id){return repo.findById(id);}

}
