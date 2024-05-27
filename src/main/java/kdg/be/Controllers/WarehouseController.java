package kdg.be.Controllers;


import kdg.be.Services.IIngredientService;
import kdg.be.Services.IngredientService;
import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.BakeryObjects.ManageIngredient;
import kdg.be.RabbitMQ.RabbitMQSender;
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

    public WarehouseController(RabbitMQSender rabbitMQSender, IngredientService ingredService) {
        this.rabbitMQSender = rabbitMQSender;
        ingredientService = ingredService;
    }

    @GetMapping("/neworder")
    public ResponseEntity<String> receiveBatch(@RequestBody String batch) {
        rabbitMQSender.SendMessage(batch);
        System.out.println(batch);
        return new ResponseEntity<String>(HttpStatusCode.valueOf(200));
    }

    @GetMapping("/ingredients")
    public List<Ingredient> allIngredients() {
        return ingredientService.findAll();
    }

    @PatchMapping("/ingredient")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Ingredient changeAmount(@RequestBody ManageIngredient manageIngredient) throws Exception {
        System.out.println(manageIngredient.getResetAmount());
        if (ingredientService.findIngredientById(manageIngredient.getIngredientId()).isPresent()) {
            ingredientService.manageIngredient(manageIngredient);
            return ingredientService.findIngredientById(manageIngredient.getIngredientId()).get();
        }
        else return new Ingredient();
    }
}
