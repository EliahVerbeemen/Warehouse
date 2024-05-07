package kdg.be.Managers;

import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.BakeryObjects.ManageIngredient;

import java.util.List;
import java.util.Optional;

public interface IIngredientManager {
    public Ingredient addIngredient(Ingredient ingredient);

    public Optional<Ingredient> findIngredientById(Long id);

    public List<Ingredient> findAll();

    public Ingredient updateIngredientAmount(Ingredient ingredient) throws Exception;

    Ingredient orderIngredients(Ingredient ingredient) throws Exception;

    Ingredient manageIngredient(ManageIngredient manageIngredient) throws Exception;
}
