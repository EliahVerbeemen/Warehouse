package kdg.be.DTO;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class OrdersFromClientDTO {







    private OrderStatus orderStatus;


    private Map<Long, Integer> products = new HashMap<>();

    @ElementCollection
    private List<String> Remarks = new ArrayList<>();






    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Integer> producs) {
        this.products = producs;
    }

    public List<String> getRemarks() {
        return Remarks;
    }

    public void setRemarks(List<String> remarks) {
        Remarks = remarks;
    }

    // Constructors
    public OrdersFromClientDTO(LocalDate orderDate, Map<Long, Integer> producten, OrderStatus orderStatus) {

        products = producten;
        this.orderStatus = orderStatus;
    }

    public OrdersFromClientDTO(Map<Long, Integer> producten) {


        products = producten;

    }

    public OrdersFromClientDTO() {

    }


    public enum OrderStatus {
        Niet_bevestigd,
        Bevestigd,
        Geannulleerd
    }
}
