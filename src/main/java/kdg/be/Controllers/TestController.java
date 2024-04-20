package kdg.be.Controllers;

import kdg.be.DTO.BatchForWarehouse;
import kdg.be.Managers.IngredientManager;
import kdg.be.Managers.ProductManager;
import kdg.be.Models.Batch;
import kdg.be.Models.BatchState;
import kdg.be.Models.Ingredient;
import kdg.be.Models.Product;
import kdg.be.RabbitMQ.RabbitSender;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TestController {

    private ProductManager productManager;
    private RabbitSender rabbitSender;

    private IngredientManager ingredientManager;
    public TestController(ProductManager productManager, RabbitSender rabbitSender, IngredientManager ingredientManager){

        this.productManager=productManager;
        this.rabbitSender=rabbitSender;
        this.ingredientManager=ingredientManager;

    }
    @GetMapping("/test")
    public List<Product> producten(){


        return productManager.getAllProducts();
    }
    @GetMapping("/message")
    public void testM(){
       List<Ingredient>ing=ingredientManager.getAllIngredients();
       List<Double>hoev=new ArrayList<>();
       hoev.add(3.2);
       hoev.add(5.1);
        Product pr=new Product("pr",new ArrayList<>(),ing,hoev);

        Product pr2=new Product("pr2",new ArrayList<>(),ing,hoev);
        HashMap<Product,Integer> pm=new HashMap<>();
        pm.put(pr,5);
        pm.put(pr2,7);
        Batch batch=new Batch();
        batch.setProductsinBatch(pm);
        batch.setBatchId(1L);
        batch.setBatchDate(LocalDate.now());
        batch.setBatchState(BatchState.InPreparation);

        Map<Ingredient,Double> toSend=new HashMap<>();

        Map<Product,Integer>products= batch.getProductsinBatch();
       //Voor elk product dat besteld is...
        products.forEach((product,numberOfProducts)->{
         List<Ingredient> ingredients=  product.getComposition();
          List<Double>  amounts=product.getAmounts();
         List<Double> ingredientMultiplyNumber=amounts.stream().map(i->i*numberOfProducts).toList();
ingredients.forEach(ingr->{


for(int i=0;i<ingredients.size();i++){

  toSend.putIfAbsent(ingredients.get(i),0.0);

  toSend.put(ingredients.get(i),
          toSend.get(ingredients.get(i))+ingredientMultiplyNumber.get(i));


}


});



        });

        BatchForWarehouse batchForWarehouse=new BatchForWarehouse(toSend.keySet().stream().toList(),toSend.values().stream().toList(),1L);



        rabbitSender.sendBatch(batchForWarehouse);

    }



}
