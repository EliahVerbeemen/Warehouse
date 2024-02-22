package com.example.warehouse2.Models;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class OutgoingOrder {

    @Id
    @GeneratedValue
    private Long outgoingOrderId;

    @ElementCollection
    private Map<Ingredient,Double> toOrder=new HashMap<>();

    private LocalDate dateOfCreation;


    private LocalDate dateOfExcecution;

    public Long getOutgoingOrderId() {
        return outgoingOrderId;
    }

    public void setOutgoingOrderId(Long outgoingOrderId) {
        this.outgoingOrderId = outgoingOrderId;
    }

    public Map<Ingredient, Double> getToOrder() {
        return toOrder;
    }

    public void setToOrder(Map<Ingredient, Double> toOrder) {
        this.toOrder = toOrder;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public LocalDate getDateOfExcecution() {
        return dateOfExcecution;
    }

    public void setDateOfExcecution(LocalDate dateOfExcecution) {
        this.dateOfExcecution = dateOfExcecution;
    }



}
