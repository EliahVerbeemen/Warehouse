package kdg.be.Managers;

import kdg.be.Models.Ingredient;
import kdg.be.Models.Product;
import kdg.be.Repositories.IProductRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductManager implements IProductManager {
    private IProductRepository repo;
    private IIngredientManager repository2;

    public ProductManager(IProductRepository repository, IIngredientManager repository2) {
        repo = repository;
        this.repository2=repository2;
    }

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Optional<Product> getProductById(long id) {
        return repo.findById(id);
    }

    public Product saveProduct(Product product) {

        return repo.save(product);
    }

    public Product saveOrUpdate(Product product) {
        System.out.println("als het product binnenkomt");
        System.out.println(product.getComposition().size());
        System.out.println(product.getAmounts().size());


        if (product.getProductId() != null && getProductById(product.getProductId()).isPresent()) {
            Product product1 = getProductById(product.getProductId()).get();
          //  product1.setAmounts(product.getAmounts());
           // product1.setComposition(product.getComposition());
            product1.setName(product.getName());
            product1.setSteps(product.getSteps());
            System.out.println("reedsAanwezig");
            product1.setComposition(product.getComposition());
            product1.setAmounts(product.getAmounts());
            product1.getComposition().forEach(e->e.getProduct().remove(product1));
            product1.setComposition(new ArrayList<>());
            product1.setComposition(product.getComposition());
            product.getComposition().forEach(e->{e.getProduct().add(product1);
                repository2.saveIngredient(e) ;
            });

            return repo.save(product1);
        } else {
       Product p1=     repo.save(product);   //Werkt niet
            System.out.println("saveOrUpdate");
product.getComposition().forEach(e->{
    e.getProduct().add(product);
    repository2.saveIngredient(e);

});

         return   p1;

        }
    }
}
