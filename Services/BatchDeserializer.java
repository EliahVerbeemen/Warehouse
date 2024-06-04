package kdg.be.Services;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import kdg.be.Models.BakeryObjects.BatchForWarehouse;
import kdg.be.Models.BakeryObjects.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BatchDeserializer extends JsonDeserializer<BatchForWarehouse> {
    private final Logger logger = LoggerFactory.getLogger(BatchDeserializer.class);
    @Override
    public BatchForWarehouse deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        try {
            JsonNode node = jp.getCodec().readTree(jp);
            logger.info("Batch Deserialization started");
            Long batchId = node.get("id").asLong();




            ObjectMapper mapper = new ObjectMapper();
            String jsonProducts = node.get("ingredients").asText();
            Ingredient[] productsArray = mapper.readValue(jsonProducts, Ingredient[].class);
            List<Ingredient> ingredients = Arrays.stream(productsArray).toList();


            String amountsString = node.get("amount").asText();
            Double[] amountsArray = mapper.readValue(amountsString, Double[].class);
            List<Double> amounts = Arrays.stream(amountsArray).toList();

            return new BatchForWarehouse(ingredients, amounts, batchId);
        } catch (Exception ex) {
            logger.error("Deserialization of the batch failed");
        }
        return new BatchForWarehouse();
    }
}

