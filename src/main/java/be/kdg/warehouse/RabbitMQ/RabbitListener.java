package be.kdg.warehouse.RabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class RabbitListener {
    private final RabbitTemplate rabbitTemplate;
    Logger logger = LoggerFactory.getLogger(RabbitListener.class);

    public RabbitListener(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "${queue.newReceptQueue}")
    public void ReceiveRecipe() {
        Object ok = this.rabbitTemplate.receive();
        System.out.println(ok);

    }

}
