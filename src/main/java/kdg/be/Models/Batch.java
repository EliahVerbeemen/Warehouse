package kdg.be.Models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Batch {


    @Id
    private Long batchId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate batchDate;


    public Batch() {

    }

    public Batch(LocalDate localDate) {

        this.batchDate = localDate;
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


    @JsonCreator
    public Batch(Map<Product, Integer> map) {
        this.productsinBatch = map;
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
