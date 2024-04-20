package kdg.be.RabbitMQ;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import kdg.be.DTO.IngredientDTO;
import kdg.be.DTO.OrdersFromClientDTO;
import kdg.be.Managers.BatchManager;
import kdg.be.Managers.ProductManager;
import kdg.be.Models.Batch;
import kdg.be.Models.BatchState;
import kdg.be.Models.Ingredient;
import kdg.be.Models.Product;
import org.slf4j.event.KeyValuePair;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.data.util.Pair;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RabbitConsumer {

    private ProductManager productManager;
    private BatchManager batchManager;

    public RabbitConsumer(ProductManager productManager, BatchManager batchManager){
    this.productManager=productManager;
    this.batchManager=batchManager;
}

    @RabbitListener(queues ="${queue.incomming}")
    public void ReceiveMessage(IngredientDTO message){

//Dit werkt
System.out.println(message.getDepartmentOfStorage());


    }

    @RabbitListener(queues = {"receiveOrder"})
    public void receiveOrder(OrdersFromClientDTO order) {
        System.out.println(order);
  Batch batch=null;
        List<Batch> batchOptional= this.batchManager.findBatchByState(BatchState.NotYetPrepared);
if(batchOptional.size()==1){

batch=batchOptional.get(0);




}
else {

    batch = batchManager.save(new Batch());
}

      for (Map.Entry<Long,Integer> keyValuePair : order.getProducts().entrySet()) {
        Optional<Product> product= productManager.getProductById(keyValuePair.getKey());
if(product.isPresent()) {
    if (batch.getProductsinBatch().containsKey(product.get())){
        batch.getProductsinBatch().replace(product.get(), batch.getProductsinBatch().get(product.get())+ keyValuePair.getValue());

        }
    else{
        batch.getProductsinBatch().put(product.get(),keyValuePair.getValue());

    }
}
batchManager.saveOrUpdate(batch);
      }











    }
    @RabbitListener(queues = "IngredientsAvailableQueue",ackMode ="MANUAL" )
    public void ReceiveBatch(Long batchId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
System.out.println("batch bevestigd");
        System.out.println(batchId);
 channel.basicAck(tag, false);


    }


}
