package kdg.be.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class IngHoeveelheidPaar {

    @Id
   @GeneratedValue
    Long IngHoeveelheidPaarId;

    double aantal;
   @OneToOne
    Ingredient ingredient;

    public IngHoeveelheidPaar(double aantal, Ingredient ingredient) {
        this.aantal = aantal;
        this.ingredient = ingredient;
    }

    public Long getTempId() {
        return this.IngHoeveelheidPaarId;
    }

    public void setTempId(Long tempId) {
        this.IngHoeveelheidPaarId = tempId;
    }

    public double getAantal() {
        return aantal;
    }

    public void setAantal(int aantal) {
        this.aantal = aantal;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public IngHoeveelheidPaar() {
    }
}
