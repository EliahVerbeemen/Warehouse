package kdg.be.Models;

import jakarta.persistence.*;
import kdg.be.Models.BakeryObjects.Ingredient;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class OutgoingOrder {

    @Id
    private Long outgoinorderId;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @CreatedDate
    private LocalDateTime batchDate;
    Long batchid;
    public boolean shortage;
    @OneToMany
    List<Ingredient> toOrder=new ArrayList<>();
    public OutgoingOrder() {
    }

    public Long getOutgoinorderId() {
        return outgoinorderId;
    }

    public void setOutgoinorderId(Long outgoinorderId) {
        this.outgoinorderId = outgoinorderId;
    }

    public LocalDateTime getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(LocalDateTime batchDate) {
        this.batchDate = batchDate;
    }

    public Long getBatchid() {
        return batchid;
    }

    public void setBatchid(Long batchid) {
        this.batchid = batchid;
    }

    public boolean isShortage() {
        return shortage;
    }

    public void setShortage(boolean shortage) {
        this.shortage = shortage;
    }

    public List<Ingredient> getToOrder() {
        return toOrder;
    }

    public void setToOrder(List<Ingredient> toOrder) {
        this.toOrder = toOrder;
    }

    public OutgoingOrder(List<Ingredient>ingredients, Long batchId, boolean shortage) {

this.batchid=batchId;
        this.toOrder=ingredients;
        this.shortage=shortage;

    }





}
