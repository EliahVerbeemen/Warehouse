package kdg.be.Controllers;


import kdg.be.Managers.IIngredientManager;
import kdg.be.Managers.IngredientManager;
import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.BakeryObjects.ManageIngredient;
import kdg.be.RabbitMQ.RabbitMQSender;
import org.hibernate.annotations.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class WarehouseController {
    //  private IIncomingOrderManager incomingOrderMgr;
    private IIngredientManager ingredientMgr;
    //   private IOutgoingOrderManager outgoingOrderMgr;
    private RabbitMQSender rabbitMQSender;

    public WarehouseController(RabbitMQSender rabbitMQSender/*, IncomingOrderManagerManager incomingOrderManager*/, IngredientManager ingredientManager/*, OutgoingOrderManager outgoingOrderManager*/) {
        this.rabbitMQSender = rabbitMQSender;
        //    incomingOrderMgr = incomingOrderManager;
        ingredientMgr = ingredientManager;
        //     outgoingOrderMgr = outgoingOrderManager;
    }

    //Warehouse moet ontvangst bevestigen, weet nog niet hoe
    @GetMapping("/neworder")
    public ResponseEntity<String> ReceiveBatch(@RequestBody String test) {
        rabbitMQSender.SendMessage(test);
        System.out.println(test);
        return new ResponseEntity<String>(HttpStatusCode.valueOf(200));
    }

    @GetMapping("/ingredients")
    public List<Ingredient> AllIngredients() {

        return ingredientMgr.findAll();
    }

    @PatchMapping("/ingredient")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Ingredient ChangeAmount(@RequestBody ManageIngredient manageIngredient) throws Exception {
System.out.println(manageIngredient.getResetAmount());
        if (ingredientMgr.findIngredientById(manageIngredient.getIngredientId()).isPresent()) {
            ingredientMgr.manageIngredient(manageIngredient);
            return ingredientMgr.findIngredientById(manageIngredient.getIngredientId()).get();


        }
        //Error
        else return new Ingredient();

    }



}
