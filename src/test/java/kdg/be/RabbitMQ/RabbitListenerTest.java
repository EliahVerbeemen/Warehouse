package kdg.be.RabbitMQ;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kdg.be.Models.BakeryObjects.BatchForWarehouse;
import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.Product;
import kdg.be.Services.IngredientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class RabbitListenerTest {

@Autowired
    private IngredientService ingredientService;

    @Autowired
    private RabbitListener rabbitListener;

    @Autowired
    private MockMvc mockMvc;

    Ingredient ingredientEen;
    Ingredient ingreientTwee;

    Map<Ingredient, Double> mapconversie;


    @BeforeAll
    public void Prepare() throws JsonProcessingException {


        Product product = new Product();
        product.setProductId(1L);
        Ingredient ingredient = new Ingredient("test", "");
        Ingredient ingredientTwee = new Ingredient("testTwee", "");


        ingredientEen = ingredient;
        this.ingreientTwee = ingredientTwee;
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredient.setIngredientId(1L);
        ingredientTwee.setIngredientId(2L);
        ingredientList.add(ingredient);
        ingredientList.add(ingredientTwee);

        product.setComposition(ingredientList);
        List<Double> hoeveelheden = new ArrayList<>();
        hoeveelheden.add(6.0);
        hoeveelheden.add(8.0);

        product.setAmounts(hoeveelheden);
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonProduct = objectMapper.writeValueAsString(product);
        //      rabbitListener.receiveRecipe(jsonProduct);


    }

    @Test
    public void ReceiveRecepyTest() {

        Assertions.assertEquals(ingredientService.findAll().size(), 2);



    }


    @Test
    public void SetPropertiesOfIngredient() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8079/api/ingredient").content("{ \"amountInStock\": 120.0, \"minimumAmountInStock\": 30.0,  \"resetAmount\":150 , \"ingredientId\":1}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(202))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.resetAmount").value(150))
                .andExpect(jsonPath("$.amountInStock").value(120))
                .andExpect(jsonPath("$.minimumAmountInStock").value(30));

        //.andReturn().getResponse().getContentType();

    }


    @Test
    public void Mapconversion() throws InterruptedException {

        // rabbitListener.receiveBatch();
        BatchForWarehouse batchForWarehouse = new BatchForWarehouse();
        List<Ingredient> ing = new ArrayList<>();
        ing.add(ingredientEen);
        ing.add(ingreientTwee);
        List<Double> amounts = new ArrayList<>();

        amounts.add(5.0);
        amounts.add(7.0);

        batchForWarehouse.setIngredients(ing);
        batchForWarehouse.setBatchId(1L);
        batchForWarehouse.setAmounts(amounts);
        Map<Ingredient, Double> mapconversie = rabbitListener.convertToMap(batchForWarehouse);
        this.mapconversie = mapconversie;
        Assertions.assertEquals(mapconversie.size(), 2);
        Assertions.assertEquals(mapconversie.get(ingredientEen), 5.0);
        Assertions.assertEquals(mapconversie.get(ingreientTwee), 7.0);


    }

    @Test
    public void receivedMapTest() throws InterruptedException {
        BatchForWarehouse batchForWarehouse = new BatchForWarehouse();
        List<Ingredient> ing = new ArrayList<>();
        ing.add(ingredientEen);
        ing.add(ingreientTwee);
        List<Double> amounts = new ArrayList<>();

        amounts.add(5.0);
        amounts.add(7.0);

        batchForWarehouse.setIngredients(ing);
        batchForWarehouse.setBatchId(1L);
        batchForWarehouse.setAmounts(amounts);
        Map<Ingredient, Double> mapconversie = rabbitListener.convertToMap(batchForWarehouse);


        AtomicBoolean b = rabbitListener.sufficientResources(mapconversie, 1L);
        boolean bo = ((boolean) b.get());
        Assertions.assertTrue(bo);


    }

    @Test
    public void DecreaseAmountTest() throws InterruptedException {
        BatchForWarehouse batchForWarehouse = new BatchForWarehouse();
        List<Ingredient> ing = new ArrayList<>();
        ing.add(ingredientEen);
        ing.add(ingreientTwee);
        List<Double> amounts = new ArrayList<>();

        amounts.add(5.0);
        amounts.add(7.0);

        batchForWarehouse.setIngredients(ing);
        batchForWarehouse.setBatchId(1L);
        batchForWarehouse.setAmounts(amounts);
        Map<Ingredient, Double> mapconversie = rabbitListener.convertToMap(batchForWarehouse);


        AtomicBoolean b = rabbitListener.sufficientResources(mapconversie, 1L);
        boolean bo = ((boolean) b.get());
        Assertions.assertTrue(bo);

        Assertions.assertEquals(this.ingredientService.findAll().size(), 2);
        Assertions.assertEquals(this.ingredientService.findIngredientById(1L).get().getAmountInStock(), 115);
        Assertions.assertEquals(this.ingredientService.findIngredientById(2L).get().getAmountInStock(), 86);


    }
}
