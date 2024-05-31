package kdg.be.Services;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import kdg.be.Models.BakeryObjects.BatchForWarehouse;
import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.Product;
import org.springframework.core.serializer.Deserializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BatchDeseriaizer extends JsonDeserializer<BatchForWarehouse> {


    @Override
    public BatchForWarehouse deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        try {
            JsonNode node = jp.getCodec().readTree(jp);
            System.out.println("test");
            Long batchId = node.get("id").asLong();
            String batchStateString = node.get("BatchState").asText();

            String dateString = node.get("batchDate").asText();
            LocalDate localDate = LocalDate.parse(dateString);

            ObjectMapper mapper = new ObjectMapper();
            String jsonProducts = node.get("ingredients").asText();
            Ingredient[] productsArray = mapper.readValue(jsonProducts, Ingredient[].class);
            List<Ingredient> ingredients = Arrays.stream(productsArray).toList();


            String amountsString = node.get("amount").asText();
            Double[] amountsArray = mapper.readValue(amountsString, Double[].class);
            List<Double> amounts = Arrays.stream(amountsArray).toList();

            return new BatchForWarehouse(ingredients, amounts, batchId);
        } catch (Exception ex) {
        }

        return new BatchForWarehouse();
    }
}

