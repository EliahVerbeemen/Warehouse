package kdg.be.Models.BakeryObjects;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import kdg.be.Models.ExpiryTypeZones;
import kdg.be.Models.Product;
import kdg.be.Models.TemperatureStorageZones;

import java.time.Duration;
import java.util.List;

@Entity
public class Ingredient {

    @ManyToMany
    // @JoinColumn(name = "item_id")
    List<Product> product;
    @Id
    private Long IngredientId;
    private String name;
    private String description;
    private double amountInStock = 100;
    private double minimumAmountInStock = 30;
    private double resetAmount = 100;
    @Enumerated(EnumType.STRING)
    private TemperatureStorageZones temperatureStorageZones;
    @Enumerated(EnumType.STRING)
    private ExpiryTypeZones expiryTypeZones;
    @Nullable
    private Duration shelfLife;
    private String dangerClass;

    public Ingredient(Long id, String name, String description) {
        this.IngredientId = id;
        this.description = description;
        this.name = name;

    }

    public Ingredient(String name, String description) {
        this.name = name;
        this.description = description;

    }

    public Ingredient() {
    }

    public Ingredient(TemperatureStorageZones temperatureStorageZones, @Nullable Duration shelfLife) {
        this.temperatureStorageZones = temperatureStorageZones;
        this.shelfLife = shelfLife;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(double amountInStock) {
        this.amountInStock = amountInStock;
    }

    public TemperatureStorageZones getTemperatureStorageZones() {
        return temperatureStorageZones;
    }

    public void setTemperatureStorageZones(TemperatureStorageZones temperatureStorageZones) {
        this.temperatureStorageZones = temperatureStorageZones;
    }

    public ExpiryTypeZones getExpiryTypeZones() {
        return expiryTypeZones;
    }

    public void setExpiryTypeZones(ExpiryTypeZones expiryTypeZones) {
        this.expiryTypeZones = expiryTypeZones;
    }

    public String getDangerClass() {
        return dangerClass;
    }

    public void setDangerClass(String dangerClass) {
        this.dangerClass = dangerClass;
    }

    public double getResetAmount() {
        return resetAmount;
    }

    public void setResetAmount(double resetAmount) {
        this.resetAmount = resetAmount;
    }

    public double getMinimumAmountInStock() {
        return minimumAmountInStock;
    }

    public void setMinimumAmountInStock(double minimumAmountInStock) {
        this.minimumAmountInStock = minimumAmountInStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIngredientId() {
        return IngredientId;
    }

    public void setIngredientId(Long ingredientId) {
        IngredientId = ingredientId;
    }

    public TemperatureStorageZones getDepartmentOfStorage() {
        return temperatureStorageZones;
    }

    public void setDepartmentOfStorage(TemperatureStorageZones temperatureStorageZones) {
        this.temperatureStorageZones = temperatureStorageZones;
    }

    public void setStorageOptions(TemperatureStorageZones temperatureStorageZone, ExpiryTypeZones expiryTypeZone) {
        this.temperatureStorageZones = temperatureStorageZone;
        this.expiryTypeZones = expiryTypeZone;
    }

    @Nullable
    public Duration getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(@Nullable Duration shelfLife) {
        this.shelfLife = shelfLife;
    }
}
