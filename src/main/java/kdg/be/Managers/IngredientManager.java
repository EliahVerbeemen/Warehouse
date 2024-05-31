package kdg.be.Managers;

import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.BakeryObjects.ManageIngredient;
import kdg.be.Repositories.IngredientRepository;
import org.springframework.stereotype.Component;

import java.util.List;
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
    @Override
    public List<Ingredient> findAll(){return repo.findAll();}

    @Override
    public Ingredient updateIngredientAmount(Ingredient ingredient) throws Exception {
     Optional<Ingredient>optionalIngredient=   repo.findById(ingredient.getIngredientId());
     if(optionalIngredient.isPresent()){
         Ingredient ingredient1=optionalIngredient.get();
         ingredient1.setAmountInStock(ingredient.getAmountInStock());
         System.out.println("check");

         System.out.println(ingredient1.getAmountInStock());

         return  repo.save(ingredient1);

     }
     else throw new Exception("Ingredient not known");




    }

    @Override
    public Ingredient orderIngredients(Ingredient ingredient) throws Exception {
        Optional<Ingredient>optionalIngredient=   repo.findById(ingredient.getIngredientId());
        if(optionalIngredient.isPresent()){
            Ingredient ingredient1=optionalIngredient.get();
            ingredient1.setAmountInStock(ingredient.getResetAmount());
            return  repo.save(ingredient1);

        }
        else throw new Exception("Ingredient not known");

    }

    @Override
    public Ingredient manageIngredient(ManageIngredient manageIngredient) throws Exception {
        Optional<Ingredient>optionalIngredient=   repo.findById(manageIngredient.getIngredientId());
        if(optionalIngredient.isPresent()){
            Ingredient ingredient=optionalIngredient.get();
           if(manageIngredient.getResetAmount()!=null){

               ingredient.setResetAmount(manageIngredient.getResetAmount());
               System.out.println(ingredient.getResetAmount());
           }
           else{
               System.out.println("null");
           }
            if(manageIngredient.getAmountInStock()!=null){

                ingredient.setAmountInStock(manageIngredient.getAmountInStock());

            }
           if(manageIngredient.getMinimumAmountInStock()!=null){

               ingredient.setMinimumAmountInStock(manageIngredient.getMinimumAmountInStock());

           }

            return  repo.save(ingredient);

        }
        else throw new Exception("Ingredient not known");

    }


}
