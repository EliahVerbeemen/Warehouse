package kdg.be.RabbitMQ;

import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.TemperatureStorageZones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class RabbitMQSender {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);
    @Value("${routingkey.outgoingOrders}")
    private String outgoingKey;
    @Value("${exchange.exchangName}")
    private String exchangename;
    private final RabbitTemplate rabbitTemplate;
    public RabbitMQSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void SendMessage(String message) {
        System.out.println(message);
        logger.debug(message);
        rabbitTemplate.convertAndSend(exchangename, outgoingKey, new Ingredient(TemperatureStorageZones.FROZEN, Duration.of(2,
                ChronoUnit.HOURS)));
    }
}
