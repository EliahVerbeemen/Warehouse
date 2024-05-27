package kdg.be.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import kdg.be.Models.BakeryObjects.Ingredient;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OutgoingOrder {

    public boolean shortage;
    Long batchid;
    @OneToMany
    List<Ingredient> toOrder = new ArrayList<>();
    @Id
    private Long outgoinorderId;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @CreatedDate
    private LocalDateTime batchDate;

    public OutgoingOrder() {
    }

    public OutgoingOrder(List<Ingredient> ingredients, Long batchId, boolean shortage) {
        this.batchid = batchId;
        this.toOrder = ingredients;
        this.shortage = shortage;
    }

    public Long getOutgoinOrderId() {
        return outgoinorderId;
    }

    public void setOutgoingOrderId(Long outgoinorderId) {
        this.outgoinorderId = outgoinorderId;
    }

    public LocalDateTime getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(LocalDateTime batchDate) {
        this.batchDate = batchDate;
    }

    public Long getBatchId() {
        return batchid;
    }

    public void setBatchId(Long batchid) {
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


}
