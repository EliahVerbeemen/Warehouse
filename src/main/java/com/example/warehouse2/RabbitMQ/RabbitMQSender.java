package com.example.warehouse2.RabbitMQ;

import com.example.warehouse2.Models.TemperatureStorageZones;
import com.example.warehouse2.Models.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class RabbitMQSender {

@Value("${routingkey.outgoingOrders}")
private String outgoingKey;
@Value("${exchange.exchangName}")
private String exchangename;

private RabbitTemplate rabbitTemplate;

private static final Logger logger= LoggerFactory.getLogger(RabbitMQSender.class);


public RabbitMQSender(RabbitTemplate rabbitTemplate){
this.rabbitTemplate=rabbitTemplate;

}

public void SendMessage(String message){
System.out.println(message);
    logger.debug(message);
rabbitTemplate.convertAndSend(exchangename,outgoingKey,new Ingredient(TemperatureStorageZones.BEVROREN, Duration.of(2,
       ChronoUnit.HOURS )));

}


}
