package kdg.be.Models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;

import static kdg.be.Models.ProductState.Nieuw;

@Entity
/*@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "productId")*/
public class Product implements Serializable {
    //Properties
    @Id
    @GeneratedValue
    private Long productId;
    private String name;
    @ElementCollection
    private List<String> steps = new ArrayList<>();
   @ManyToMany(cascade = CascadeType.ALL,fetch= FetchType.EAGER
     ,mappedBy = "product")
    private List<Ingredient> composition = new ArrayList<>();

    @ElementCollection
    private List<Double> amounts = new ArrayList<>();


    private ProductState _ProductStatus = Nieuw;

    //Constructors
    public Product() {
    }

    public Product(String name, List<String> steps) {
        this.name = name;
        this.steps = steps;
    }

    public Product(String name, List<String> steps, List<Ingredient> composition,List<Double>amounts) {
        this.name = name;
        this.steps = steps;
        this.composition = composition;
        this.amounts=amounts;
    }

    public List<Ingredient> getComposition() {
        return composition;
    }

    public void setComposition(List<Ingredient> composition) {
        this.composition = composition;
    }

    public List<Double> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<Double> amounts) {
        this.amounts = amounts;
    }
// @JsonDeserialize(keyUsing = IngredientDeserializer.class)
    //GET & SET


    public ProductState get_ProductStatus() {
        return _ProductStatus;
    }

    public void set_ProductStatus(ProductState _ProductStatus) {
        this._ProductStatus = _ProductStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductState getProductStatus() {
        return _ProductStatus;
    }

    public void setProductStatus(ProductState productStatus) {
        _ProductStatus = productStatus;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
