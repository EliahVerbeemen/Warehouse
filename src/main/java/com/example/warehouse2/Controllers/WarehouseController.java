package com.example.warehouse2.Controllers;

import com.example.warehouse2.RabbitMQ.RabbitMQSender;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WarehouseController {

    RabbitMQSender rabbitMQSender;


    public WarehouseController(RabbitMQSender rabbitMQSender){

        this.rabbitMQSender=rabbitMQSender;

    }

    //Warehouse moet ontvangst bevestigen, weet nog niet hoe
    @GetMapping("/neworder")
    public ResponseEntity<String> ReceiveBatch(@RequestBody String test){
rabbitMQSender.SendMessage(test);
System.out.println(test);
return new ResponseEntity<String>(HttpStatusCode.valueOf(200));
    }


}
