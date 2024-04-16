package kdg.be.Managers;

import kdg.be.Models.BakeryObjects.Ingredient;

import java.util.Optional;

public interface IIngredientManager {
    public Ingredient addIngredient(Ingredient ingredient);

    public Optional<Ingredient> findIngredientById(Long id);

}
