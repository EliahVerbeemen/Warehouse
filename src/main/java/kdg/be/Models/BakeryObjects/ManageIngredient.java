package kdg.be.Models.BakeryObjects;

import java.util.Optional;

public class ManageIngredient {
    private Double amountInStock;
    private Double minimumAmountInStock;
    private Double resetAmount;
    private Long ingredientId;

    public ManageIngredient(Optional<Double> amountInStock, Optional<Double> minimumAmountInStock, Optional<Double> resetAmount, Long ingredientId) {
        amountInStock.ifPresent(aDouble -> this.amountInStock = aDouble);
        minimumAmountInStock.ifPresent(aDouble -> this.minimumAmountInStock = aDouble);
        resetAmount.ifPresent(aDouble -> this.resetAmount = aDouble);
        this.ingredientId=ingredientId;
    }

    public ManageIngredient() {
    }

    public Double getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(double amountInStock) {
        this.amountInStock = amountInStock;
    }

    public Double getMinimumAmountInStock() {
        return minimumAmountInStock;
    }

    public void setMinimumAmountInStock(double minimumAmountInStock) {
        this.minimumAmountInStock = minimumAmountInStock;
    }

    public Double getResetAmount() {
        return resetAmount;
    }

    public void setResetAmount(double resetAmount) {
        this.resetAmount = resetAmount;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }
}
