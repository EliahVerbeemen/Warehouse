package kdg.be.Models;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Batch {

   private BatchState batchState=BatchState.NotYetPrepared;

    public BatchState getBatchState() {
        return batchState;
    }

    public void setBatchState(BatchState batchState) {
        this.batchState = batchState;
    }

    @Id
    @GeneratedValue
    private Long batchId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate batchDate;


    public Batch() {

    }

    public Batch(LocalDate localDate) {

        this.batchDate = localDate;
    }
    public Batch(BatchState batchState) {

        this.batchState = batchState;
    }
    public Map<Product, Integer> getProductsinBatch() {
        return productsinBatch;
    }

    public void setProductsinBatch(Map<Product, Integer> productsinBatch) {
        this.productsinBatch = productsinBatch;
    }

    @ElementCollection(fetch = FetchType.EAGER)
private Map<Product,Integer>productsinBatch=new HashMap<>();
    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public LocalDate getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(LocalDate batchdatum) {
        batchDate = batchdatum;
    }

}
