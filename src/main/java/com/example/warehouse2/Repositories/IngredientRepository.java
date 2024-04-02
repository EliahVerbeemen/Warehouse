package com.example.warehouse2.Repositories;

import com.example.warehouse2.Models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {
}
