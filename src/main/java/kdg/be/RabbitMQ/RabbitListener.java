package kdg.be.RabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import kdg.be.Models.BakeryObjects.BatchForWarehouse;
import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.OutgoingOrder;
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

    private final Logger logger=LoggerFactory.getLogger(RabbitListener.class);


    public RabbitListener(RabbitTemplate rabbitTemplate, IngredientService ingredientService) {
        this.rabbitTemplate = rabbitTemplate;
        this.ingredientService = ingredientService;
    }

    private List<Ingredient> jsonToProduct(String jsonProduct) throws JsonProcessingException {
        List<Ingredient> ingredients = new ArrayList<>();

        JsonNode node = new ObjectMapper().readTree(jsonProduct);
        JsonNode composition = node.get("composition");
        if (composition.isArray()) {
            ArrayNode compositionArray = (ArrayNode) composition;
            compositionArray.forEach((ing) -> {
                Long id = ing.get("ingredientId").asLong();
                String name = ing.get("name").asText();
                String beschrijving = ing.get("beschrijving").asText();
                ingredients.add(new Ingredient(id, name, beschrijving));
            });
        }

        return ingredients;
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "warehouseQueu")
    public void receiveRecipe(String jsonProduct) throws JsonProcessingException {

        List<Ingredient> ingredientsInProduct = new ArrayList<>();
        try {
            ingredientsInProduct = jsonToProduct(jsonProduct);
        } catch (JsonProcessingException exception) {
            logger.warn("Incoming product could not be deserialized");
        }
        ingredientsInProduct.forEach(i -> {
            if (ingredientService.findIngredientById(i.getIngredientId()).isEmpty()) {
                ingredientService.addIngredient(i);
            }
        });
    }

    private BatchForWarehouse jsonStringToBatch(String jsonBatchForWarehouse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonBatchForWarehouse, BatchForWarehouse.class);
        } catch (Exception ex) {
                logger.warn("batch could not be deserialised");
        }
        return new BatchForWarehouse();
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "BatchQueu")
    public void ReceiveBatch(String batch) throws InterruptedException, JsonProcessingException {

       BatchForWarehouse batchForWarehouse= jsonStringToBatch(batch);

       Map<Ingredient,Double>ingredientmap=convertToMap(batchForWarehouse);

        ingredientmap.keySet().forEach(i -> {
                    if (ingredientService.findIngredientById(i.getIngredientId()).isEmpty()) {
                        ingredientService.addIngredient(i);
                    }
                });



            sufficientResources(ingredientmap, batchForWarehouse.getBatchId());

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

                logger.info(String.valueOf(ingredient.getAmountInStock()));
                logger.info(String.valueOf(amount));


                if (ingredient.getAmountInStock() < amount) {
                    sufficient.set(false);
                    ingredientsToOrder.add(ingredient);
                } else if (ingredient.getAmountInStock() - amount < ingredient.getMinimumAmountInStock()) {
                    ingredientsToOrder.add(ingredient);
                }
            } else {
                logger.warn("Ingredient not in stock!");

            }

        });
        if (!ingredientsToOrder.isEmpty()) {
            createOutgoingOrder(new OutgoingOrder(ingredientsToOrder, batchId, !sufficient.get()));
            logger.info("additional ingredients have een ordered");
        }
        if (sufficient.get()) {
            rabbitTemplate.convertAndSend("ingAvailableExchange", "", batchId);
            decreaseAmounts(map);

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
                    logger.warn("stock could not be updated");
                }
            } else {
                logger.warn("unknown ingredient used in product");


            }

        });
    }

    public void createOutgoingOrder(OutgoingOrder outgoingOrder) throws InterruptedException {
        logger.info("Outgoing order created");
        TimeUnit.SECONDS.sleep(15);
        outgoingOrder.getToOrder().forEach(ing -> {
            try {
                ingredientService.orderIngredients(ing);
                rabbitTemplate.convertAndSend("ingAvailableExchange", "", outgoingOrder.getBatchid());
            } catch (Exception e) {
                logger.warn("order could not be confirmed");
            }
        });
    }
}
