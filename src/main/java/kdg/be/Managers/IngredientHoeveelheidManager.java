package kdg.be.Managers;

import kdg.be.Models.IngHoeveelheidPaar;
import kdg.be.Repositories.IIngredientHoeveelheidRepository;
import org.springframework.stereotype.Component;

@Component
public class IngredientHoeveelheidManager implements IIngredientHoeveelheidManager{
    public IIngredientHoeveelheidRepository iIngredientHoeveelheidRepository;

    public IngredientHoeveelheidManager(IIngredientHoeveelheidRepository iIngredientHoeveelheidRepository){

        this.iIngredientHoeveelheidRepository=iIngredientHoeveelheidRepository;
    }

    @Override
    public IngHoeveelheidPaar saveIngHoeveelheid(IngHoeveelheidPaar ingHoeveelheidPaar) {
        return iIngredientHoeveelheidRepository.save(ingHoeveelheidPaar);
    }





}
