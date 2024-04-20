package kdg.be.Managers;

import kdg.be.Models.IngHoeveelheidPaar;
import org.springframework.stereotype.Component;

@Component
public interface IIngredientHoeveelheidManager {

    public IngHoeveelheidPaar saveIngHoeveelheid(IngHoeveelheidPaar ingHoeveelheidPaar);

}
