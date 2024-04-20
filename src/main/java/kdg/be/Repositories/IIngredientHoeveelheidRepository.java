package kdg.be.Repositories;

import kdg.be.Models.IngHoeveelheidPaar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IIngredientHoeveelheidRepository extends JpaRepository<IngHoeveelheidPaar,Long> {
}
