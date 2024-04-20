package kdg.be.Repositories;

import kdg.be.Models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IIngredientRepository extends JpaRepository<Ingredient, Long> {

}
