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
    private static final Logger log = LoggerFactory.getLogger(RabbitListener.class);
    private final RabbitTemplate rabbitTemplate;
    private final IngredientService ingredientService;

    public RabbitListener(RabbitTemplate rabbitTemplate, IngredientService ingredientService) {
        this.rabbitTemplate = rabbitTemplate;
        this.ingredientService = ingredientService;
    }

    private List<Ingredient> jsonToProduct(String jsonProduct) throws JsonProcessingException {
        List<Ingredient> ingredients = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
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
        System.out.println(composition);
        return ingredients;
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "warehouseQueu")
    public void receiveRecipe(String jsonProduct) throws JsonProcessingException {
        System.out.println(jsonProduct);
        List<Ingredient> ingredientsInProduct = new ArrayList<>();
        try {
            ingredientsInProduct = jsonToProduct(jsonProduct);
        } catch (JsonProcessingException exception) {
            log.warn("Incoming product cannot be deserialized");
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
            BatchForWarehouse batchForWarehouse = objectMapper.readValue(jsonBatchForWarehouse, BatchForWarehouse.class);
        } catch (Exception ex) {
            log.error("Error performing jsonStringToBatch");
        }
        return new BatchForWarehouse();
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "BatchQueu")
    public void ReceiveBatch(String batch) throws InterruptedException, JsonProcessingException {
        System.out.println(batch);
        jsonStringToBatch(batch);
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
                log.warn("Ingredient not in stock!");

            }

        });
        if (!ingredientsToOrder.isEmpty()) {
            createOutgoingOrder(new OutgoingOrder(ingredientsToOrder, batchId, !sufficient.get()));
        }
        if (sufficient.get()) {
            rabbitTemplate.convertAndSend("ingAvailableExchange", "", batchId);
            decreaseAmounts(map);
            System.out.println("Sufficient");
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
                throw new RuntimeException("Ingredient not present");
            }
            System.out.println("Decrease executed");
        });
    }

    public void createOutgoingOrder(OutgoingOrder outgoingOrder) throws InterruptedException {
        System.out.println("Outgoing order created");
        TimeUnit.SECONDS.sleep(15);
        outgoingOrder.getToOrder().forEach(ing -> {
            try {
                ingredientService.orderIngredients(ing);
                rabbitTemplate.convertAndSend("ingAvailableExchange", "", outgoingOrder.getBatchid());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
