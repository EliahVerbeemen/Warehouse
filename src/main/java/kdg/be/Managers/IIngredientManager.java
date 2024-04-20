package kdg.be.Managers;

import kdg.be.Models.Ingredient;

import java.util.List;
import java.util.Optional;


public interface IIngredientManager {
    public List<Ingredient> getAllIngredients();
    public Optional<Ingredient> getIngredientById(Long id);
    public Ingredient saveIngredient(Ingredient ingredient);
    public void deleteIngredient(Long id);
    public void updateIngredient(Long id, Ingredient nieuwIngredient);
}
