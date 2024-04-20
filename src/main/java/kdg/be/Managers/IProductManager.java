package kdg.be.Managers;

import kdg.be.Models.Product;

import java.util.List;
import java.util.Optional;

public interface IProductManager {
    public Optional<Product> getProductById(long id);
    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Product saveOrUpdate(Product product);
}
