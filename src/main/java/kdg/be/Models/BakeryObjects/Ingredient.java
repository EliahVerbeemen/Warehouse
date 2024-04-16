package kdg.be.Models.BakeryObjects;

import kdg.be.Models.ExpiryTypeZones;
import kdg.be.Models.Product;
import kdg.be.Models.TemperatureStorageZones;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.Duration;
import java.util.List;

@Entity
public class Ingredient {

    @Id
    private Long IngredientId;


    @ManyToMany
    // @JoinColumn(name = "item_id")
    List<Product> product;
    private String name;

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    private String description;


    public Ingredient(String name, String description) {
        this.name = name;
        this.description = description;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschrijving() {
        return description;
    }

    public void setBeschrijving(String beschrijving) {
        description = beschrijving;
    }






















    @Enumerated(EnumType.STRING)
    private TemperatureStorageZones temperatureStorageZones;
    @Enumerated(EnumType.STRING)
    private ExpiryTypeZones expiryTypeZones;

    @Nullable
    private Duration shelfLife;

    private String dangerClass;

    public Ingredient() {
    }

    public Ingredient(TemperatureStorageZones temperatureStorageZones, @Nullable Duration shelfLife) {
        this.temperatureStorageZones = temperatureStorageZones;
        this.shelfLife = shelfLife;
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
