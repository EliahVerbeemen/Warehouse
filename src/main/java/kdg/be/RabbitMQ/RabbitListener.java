package kdg.be.RabbitMQ;

import kdg.be.Managers.IngredientManager;
import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
public class RabbitListener {
    private final RabbitTemplate rabbitTemplate;
    private final IngredientManager ingredientManager;

    Logger logger = LoggerFactory.getLogger(RabbitListener.class);

    public RabbitListener(RabbitTemplate rabbitTemplate, IngredientManager ingredientManager) {

        this.rabbitTemplate = rabbitTemplate;
        this.ingredientManager = ingredientManager;
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "warehouseQueu")
    public void ReceiveRecipe(Product product) {
        System.out.println(product.getComposition().size());
//Op dit ogenblik een list
        product.getComposition().forEach(i -> {
            System.out.println(i);
            if (ingredientManager.findIngredientById(i.getIngredientId()).isEmpty()) {
                System.out.println("opgeslagen");
                ingredientManager.addIngredient(i);

            } else {
                System.out.println(product.getComposition().size());
                System.out.println(i.getIngredientId());

            }


        });
        System.out.println(product.getName());

    }







  /*  @org.springframework.amqp.rabbit.annotation.RabbitListener(queues ={"amq.rabbitmq.reply-to"},ackMode = "NONE")
    @SendTo("batchQueu")
    public Message<String> ReceiveBatch(Batch batch){


        return org.springframework.messaging.support.MessageBuilder.withPayload("check")
                .setHeader(AmqpHeaders.REPLY_TO, "batchQueu")
                .build();


    }
            */

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "BatchQueu")
    //   @SendTo("IngredientsAvailableQueue")
    public void ReceiveBatch(List<Ingredient> batch) {

        System.out.println(batch);

        MessagePostProcessor messagePostProcessor = message -> {
            MessageProperties messageProperties
                    = message.getMessageProperties();
            messageProperties.setReplyTo("IngredientsAvailableQueue");
            return message;
        };

        rabbitTemplate.convertSendAndReceive("", batch, messagePostProcessor);


    }
}
