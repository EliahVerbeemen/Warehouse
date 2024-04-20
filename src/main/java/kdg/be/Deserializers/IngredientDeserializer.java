package kdg.be.Deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import kdg.be.Models.Ingredient;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;

import java.io.IOException;

public class IngredientDeserializer extends KeyDeserializer {


    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return new Ingredient("test","beschrijving");
    }
}
