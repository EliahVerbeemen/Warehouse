package com.example.warehouse2.RabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class RabitListener {
    Logger logger = LoggerFactory.getLogger(RabitListener.class);
    private final RabbitTemplate rabbitTemplate;

    public RabitListener(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${queue.newReceptQueue}")
    public void ReceiveRecepy() {
        Object ok = this.rabbitTemplate.receive();
        System.out.println(ok);

    }

}
