package kdg.be.RabbitMQ;

import kdg.be.Managers.IngredientManager;
import kdg.be.Models.BakeryObjects.BatchForWarehouse;
import kdg.be.Models.BakeryObjects.Ingredient;
import kdg.be.Models.OutgoingOrder;
import kdg.be.Models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


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


        product.getComposition().forEach(i -> {
            if (ingredientManager.findIngredientById(i.getIngredientId()).isEmpty()) {

                ingredientManager.addIngredient(i);

            }
        });


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
    public void ReceiveBatch(BatchForWarehouse batch) throws InterruptedException {

Map<Ingredient,Double>ingedientMap=convertToMap(batch);

        if(sufficientResources(ingedientMap, batch.getBatchId()).get()){




        }




    }

    public Map<Ingredient,Double> convertToMap(BatchForWarehouse batch){

        Map<Ingredient,Double>ingredientMap=new HashMap<>();
        for(int i=0;i<batch.getIngredients().size();i++){

            ingredientMap.put(batch.getIngredients().get(i),batch.getAmounts().get(i));

        }
        return ingredientMap;
    }


    public AtomicBoolean sufficientResources(Map<Ingredient,Double> map, Long batchId) throws InterruptedException {

        List<Ingredient>ingredientsToOrder=new ArrayList<>();
        AtomicBoolean sufficient= new AtomicBoolean(true);

        map.forEach((ing,amount)->{

        Optional<Ingredient>optionalIngredient= ingredientManager.findIngredientById(ing.getIngredientId())   ;
         if(optionalIngredient.isPresent()) {

             Ingredient ingredient=optionalIngredient.get();
          //Er is een tekort

             if(ingredient.getAmountInStock()<amount){
               sufficient.set(false);
                ingredientsToOrder.add(ingredient);
             } else if (ingredient.getAmountInStock()-amount< ingredient.getMinimumAmountInStock()) {
                 ingredientsToOrder.add(ingredient);

             }



         }
         else{

             logger.warn("Ingredient niet aanwezig!");

         }

      });

if(ingredientsToOrder.size()>0){

    createOutgoingOrder(new OutgoingOrder(ingredientsToOrder,batchId, !sufficient.get()));

}

if(sufficient.get()){

    rabbitTemplate.convertAndSend("ingAvailableExchange","",batchId);

    decreaseAmounts(map);
System.out.println("voldoende");
}
return sufficient;

    }

    public void decreaseAmounts(Map<Ingredient,Double>outgoingIngredients){
        outgoingIngredients.forEach((ing,amount)->{

          Optional<Ingredient>optionalIngredient=  ingredientManager.findIngredientById(ing.getIngredientId());
          if(optionalIngredient.isPresent()){
              Ingredient ingredient=optionalIngredient.get();
              ingredient.setAmountInStock(ingredient.getAmountInStock()-amount);
              try {
                  ingredientManager.updateIngredientAmount(ingredient);
              } catch (Exception e) {
                  throw new RuntimeException(e);
              }

          }
          else{

              throw new RuntimeException("ingredient not present");
          }


            System.out.println("decrease toegepast");







        });



    }

    public void createOutgoingOrder(OutgoingOrder outgoingOrder) throws InterruptedException {

System.out.println("outgoing order gecreërd");

        TimeUnit.SECONDS.sleep(15);
outgoingOrder.getToOrder().forEach(ing->{

    try {
        ingredientManager.orderIngredients(ing);

       rabbitTemplate.convertAndSend("ingAvailableExchange","",outgoingOrder.getBatchid());

    } catch (Exception e) {
        throw new RuntimeException(e);
    }

});


    }

}
