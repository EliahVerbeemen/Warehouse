package be.kdg.warehouse.Models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.Duration;

@Entity
public class Ingredient {

    @Id
    @GeneratedValue
    private Long IngredientId;
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
