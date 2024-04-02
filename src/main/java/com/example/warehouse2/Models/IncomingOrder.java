package com.example.warehouse2.Models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class IncomingOrder {

    @Id
    @GeneratedValue
    private Long incomingOrderId;

    @ElementCollection
    private Map<Ingredient,Double> ordered=new HashMap<>();

    private LocalDate dateOfCreation;

    public Map<Ingredient, Double> getOrdered() {
        return ordered;
    }

    public void setOrdered(Map<Ingredient, Double> ordered) {
        this.ordered = ordered;
    }

    private LocalDate dateOfArrival;

    public Long getIncomingOrderId() {
        return incomingOrderId;
    }

    public void setIncomingOrderId(Long incomingOrderId) {
        this.incomingOrderId = incomingOrderId;
    }

    public LocalDate getDateOfArrival() {
        return dateOfArrival;
    }

    public void setDateOfArrival(LocalDate dateOfArrival) {
        this.dateOfArrival = dateOfArrival;
    }



    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public LocalDate getDateOfExcecution() {
        return dateOfArrival;
    }

    public void setDateOfExcecution(LocalDate dateOfExcecution) {
        this.dateOfArrival = dateOfExcecution;
    }

}
