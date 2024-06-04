package kdg.be.Repositories;

import kdg.be.Models.BakeryObjects.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {
}
