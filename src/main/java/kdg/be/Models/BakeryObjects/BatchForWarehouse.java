package kdg.be.Models.BakeryObjects;





import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import kdg.be.Services.BatchDeserializer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@JsonDeserialize(using = BatchDeserializer.class)
@Data
public class BatchForWarehouse {
    List<Ingredient> ingredients=new ArrayList<>();
    List<Double>Amounts=new ArrayList<>();
    Long batchId;

    public BatchForWarehouse() {
    }

    public BatchForWarehouse(List<Ingredient> ingredients, List<Double> amounts, Long batchId) {
        this.ingredients = ingredients;
        this.Amounts = amounts;
        this.batchId = batchId;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Double> getAmounts() {
        return Amounts;
    }

    public void setAmounts(List<Double> amounts) {
        Amounts = amounts;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }
}
