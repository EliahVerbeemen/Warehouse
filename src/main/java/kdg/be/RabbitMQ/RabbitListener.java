package kdg.be.RabbitMQ;

import kdg.be.Models.BakeryObjects.BatchForWarehouse;
import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.OutgoingOrder;
import kdg.be.Models.Product;
import kdg.be.Services.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
public class RabbitListener {
    private final RabbitTemplate rabbitTemplate;
    private final IngredientService ingredientService;

    Logger logger = LoggerFactory.getLogger(RabbitListener.class);

    public RabbitListener(RabbitTemplate rabbitTemplate, IngredientService ingredientService) {

        this.rabbitTemplate = rabbitTemplate;
        this.ingredientService = ingredientService;
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "warehouseQueu")
    public void ReceiveRecipe(Product product) {


        product.getComposition().forEach(i -> {
            if (ingredientService.findIngredientById(i.getIngredientId()).isEmpty()) {

                ingredientService.addIngredient(i);

            }
        });


    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "BatchQueu")
    public void ReceiveBatch(BatchForWarehouse batch) throws InterruptedException {

        Map<Ingredient, Double> ingedientMap = convertToMap(batch);

    }

    public Map<Ingredient, Double> convertToMap(BatchForWarehouse batch) {
        Map<Ingredient, Double> ingredientMap = new HashMap<>();
        for (int i = 0; i < batch.getIngredients().size(); i++) {
            ingredientMap.put(batch.getIngredients().get(i), batch.getAmounts().get(i));
        }
        return ingredientMap;
    }


    public AtomicBoolean sufficientResources(Map<Ingredient, Double> map, Long batchId) throws InterruptedException {
        List<Ingredient> ingredientsToOrder = new ArrayList<>();
        AtomicBoolean sufficient = new AtomicBoolean(true);

        map.forEach((ing, amount) -> {
            Optional<Ingredient> optionalIngredient = ingredientService.findIngredientById(ing.getIngredientId());
            if (optionalIngredient.isPresent()) {
                Ingredient ingredient = optionalIngredient.get();
                if (ingredient.getAmountInStock() < amount) {
                    sufficient.set(false);
                    ingredientsToOrder.add(ingredient);
                } else if (ingredient.getAmountInStock() - amount < ingredient.getMinimumAmountInStock()) {
                    ingredientsToOrder.add(ingredient);
                }
            } else {
                logger.warn("Ingredient niet aanwezig!");
            }
        });

        if (!ingredientsToOrder.isEmpty()) {
            createOutgoingOrder(new OutgoingOrder(ingredientsToOrder, batchId, !sufficient.get()));
        }

        if (sufficient.get()) {
            rabbitTemplate.convertAndSend("ingAvailableExchange", "", batchId);
            decreaseAmounts(map);
            System.out.println("voldoende");
        }
        return sufficient;

    }

    public void decreaseAmounts(Map<Ingredient, Double> outgoingIngredients) {
        outgoingIngredients.forEach((ing, amount) -> {
            Optional<Ingredient> optionalIngredient = ingredientService.findIngredientById(ing.getIngredientId());
            if (optionalIngredient.isPresent()) {
                Ingredient ingredient = optionalIngredient.get();
                ingredient.setAmountInStock(ingredient.getAmountInStock() - amount);
                try {
                    ingredientService.updateIngredientAmount(ingredient);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("ingredient not present");
            }
            System.out.println("decrease toegepast");
        });
    }

    public void createOutgoingOrder(OutgoingOrder outgoingOrder) throws InterruptedException {
        System.out.println("outgoing order gecreërd");
        TimeUnit.SECONDS.sleep(15);
        outgoingOrder.getToOrder().forEach(ing -> {
            try {
                ingredientService.orderIngredients(ing);
                rabbitTemplate.convertAndSend("ingAvailableExchange", "", outgoingOrder.getBatchId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
