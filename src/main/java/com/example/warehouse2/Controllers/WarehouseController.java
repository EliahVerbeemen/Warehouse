package com.example.warehouse2.Controllers;

import com.example.warehouse2.Managers.*;
import com.example.warehouse2.RabbitMQ.RabbitMQSender;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WarehouseController {
    private IIncomingOrderManager incomingOrderMgr;
    private IIngredientManager ingredientMgr;
    private IOutgoingOrderManager outgoingOrderMgr;
    private RabbitMQSender rabbitMQSender;

    public WarehouseController(RabbitMQSender rabbitMQSender, IncomingOrderManagerManager incomingOrderManager, IngredientManager ingredientManager, OutgoingOrderManager outgoingOrderManager) {
        this.rabbitMQSender = rabbitMQSender;
        incomingOrderMgr = incomingOrderManager;
        ingredientMgr = ingredientManager;
        outgoingOrderMgr = outgoingOrderManager;
    }

    //Warehouse moet ontvangst bevestigen, weet nog niet hoe
    @GetMapping("/neworder")
    public ResponseEntity<String> ReceiveBatch(@RequestBody String test) {
        rabbitMQSender.SendMessage(test);
        System.out.println(test);
        return new ResponseEntity<String>(HttpStatusCode.valueOf(200));
    }


}
