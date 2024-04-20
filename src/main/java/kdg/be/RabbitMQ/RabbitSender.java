package kdg.be.RabbitMQ;


//Klasse om nieuwe recepies te zenden

import kdg.be.DTO.BatchForWarehouse;
import kdg.be.Models.Batch;
import kdg.be.Models.Ingredient;
import kdg.be.Models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RabbitSender {


    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RabbitSender.class);


    public RabbitSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

    }

    public void sendNewRecipe(Product product) {

        System.out.println("product verzonden");
        System.out.println(product.getComposition().size());
        System.out.println(product.getAmounts().size());
//product.setComposition(new ArrayList<>());


        rabbitTemplate.convertAndSend("newRecepyExchange", "warehouseQueu", product);
        rabbitTemplate.convertSendAndReceive("newRecepyExchange", "newRecepiesQueue", product);

    }

    public void sendBatch(BatchForWarehouse batch) {
     /*   System.out.println("De batch bevat " + batch.getProductsinBatch().size() + "producten");

        batch.getProductsinBatch().forEach((p, v) -> System.out.println("Aantal ingedienten" + p.getComposition().size()));
        Batch b = new Batch();
        HashMap<Product, Integer> h = new HashMap<>();
        Product p = new Product("ty", new ArrayList<String>());
        Ingredient i = new Ingredient("tu", "");
        i.setIngredientId(1L);
        p.getComposition().add(i);
        h.put(p, 4);
        batch.setProductsinBatch(h);
        Map<Product, Integer> products = new HashMap<>();

        batch.getProductsinBatch();


        Map<Ingredient, Double> ingredients = new HashMap<>();




    ;



System.out.println("Aantal ingreienten");       System.out.println(ingredients.size());
*/

        rabbitTemplate.convertSendAndReceive("batchExchange","batchKey",batch);
    }









}
