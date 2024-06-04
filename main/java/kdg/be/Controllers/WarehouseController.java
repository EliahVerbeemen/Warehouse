package kdg.be.Controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import kdg.be.Models.BakeryObjects.BatchForWarehouse;
import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.BakeryObjects.ManageIngredient;
import kdg.be.RabbitMQ.RabbitMQSender;
import kdg.be.Services.IIngredientService;
import kdg.be.Services.IngredientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WarehouseController {
    private final IIngredientService ingredientService;
    private final RabbitMQSender rabbitMQSender;
    private final Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    public WarehouseController(RabbitMQSender rabbitMQSender, IngredientService ingredientService) {
        this.rabbitMQSender = rabbitMQSender;
        this.ingredientService = ingredientService;
    }


    @GetMapping("/neworder")
    public ResponseEntity<String> receiveBatch(@RequestBody String newOrder) {
        rabbitMQSender.SendMessage(newOrder);
        logger.info(newOrder);
        return new ResponseEntity<String>(HttpStatusCode.valueOf(200));
    }

    @GetMapping("/ingredients")
    public List<Ingredient> allIngredients() {
        return ingredientService.findAll();
    }

    @PatchMapping("/ingredient")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Ingredient changeAmount(@RequestBody ManageIngredient manageIngredient) throws Exception {
        logger.debug(""+manageIngredient.getResetAmount());
        if (ingredientService.findIngredientById(manageIngredient.getIngredientId()).isPresent()) {
            ingredientService.manageIngredient(manageIngredient);
            return ingredientService.findIngredientById(manageIngredient.getIngredientId()).get();
        } else return new Ingredient();
    }

    @GetMapping
    public BatchForWarehouse jsonToBatchForWarehouse() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new BatchForWarehouse();
    }
}
