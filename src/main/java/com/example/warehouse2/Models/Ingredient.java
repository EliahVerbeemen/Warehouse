package com.example.warehouse2.Models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.Duration;

@Entity
public class Ingredient {

    @Id
    @GeneratedValue
    private Long IngredientId;


   @Enumerated(EnumType.STRING)
    private DepartmentOfStorage departmentOfStorage;

    @Nullable
    private Duration shelfLife;

    public Ingredient() {
    }

    public Ingredient(DepartmentOfStorage departmentOfStorage, @Nullable Duration shelfLife) {
        this.departmentOfStorage = departmentOfStorage;
        this.shelfLife = shelfLife;
    }

    public Long getIngredientId() {
        return IngredientId;
    }

    public void setIngredientId(Long ingredientId) {
        IngredientId = ingredientId;
    }

    public DepartmentOfStorage getDepartmentOfStorage() {
        return departmentOfStorage;
    }

    public void setDepartmentOfStorage(DepartmentOfStorage departmentOfStorage) {
        this.departmentOfStorage = departmentOfStorage;
    }

    @Nullable
    public Duration getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(@Nullable Duration shelfLife) {
        this.shelfLife = shelfLife;
    }
}
