package be.kdg.warehouse.Repositories;

import be.kdg.warehouse.Models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {
}
