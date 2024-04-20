package kdg.be.RabbitMQ;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfiguratie {

    @Value("${queue.newReceptQueue}")
    private String newRecepyQueue;

    @Value("${queue.deactivating}")
    private String deactivateRecepyQueue;

    @Value("${exchange.newRecepyExchange}")
    private String exchangeName;



    //Kies de juiste import voor de queue

    @Qualifier(value = "RecepyForClient")
    @Bean
    public Queue queue(){

        return new Queue("clientQueu",true);


    }
//https://stackoverflow.com/questions/55302971/setting-reply-to-properties-in-rabbitlistener
    @Qualifier(value = "BatchQueu")
    @Bean
    public Queue batchqueue(){

        return new Queue("BatchQueu",true);


    }

   /* @Qualifier(value = "ReceiveBatchQueu")
    @Bean
    public Queue reeceiveBatchQueu(){

        return new Queue("receiveBatchQueu",true);


    }*/
    @Qualifier("batchExchange")
    @Bean
public DirectExchange directExchange(){


        return new DirectExchange("batchExchange",true,false);

}



@Bean
    public Binding b( @Qualifier("batchExchange")DirectExchange directExchange,@Qualifier(value = "BatchQueu")Queue batchqueue){


        return  BindingBuilder.bind(batchqueue).to(directExchange).with("batchKey");

    }






//2 queus en 1 exchange voor recepies
  @Qualifier(value = "RecepyForWarehouse")
  @Bean
    public Queue recepyForWarehouse(){

        return new Queue("warehouseQueu",true);


    }
@Bean
@Qualifier("recepyExchange")
    public DirectExchange recepyForClientExchange(){

        return new DirectExchange(exchangeName);


    }

    @Bean

    public Binding binding(@Qualifier("RecepyForClient") Queue queue,@Qualifier("recepyExchange") DirectExchange directExchange){

        return BindingBuilder.bind(queue).to(directExchange).withQueueName();


    }

    @Bean

    public Binding bindingTwo(@Qualifier("recepyExchange")DirectExchange directExchange,@Qualifier("RecepyForWarehouse") Queue recepyQueu){

        return BindingBuilder.bind(recepyQueu).to(directExchange).withQueueName();


    }
    /*
@Bean
   public Binding topicBinding(@Qualifier("deactivate") Queue Deactivate,TopicExchange topicExchange){
        return    BindingBuilder.bind(Deactivate).to(topicExchange).with("deactivate");

    }*/
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){

        RabbitTemplate rabbitTemplate= new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
rabbitTemplate.containerAckMode(AcknowledgeMode.NONE);
        return rabbitTemplate;
    }
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {

        ObjectMapper objectMapper=new ObjectMapper();


        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("sparrow.rmq.cloudamqp.com");
        connectionFactory.setUsername("umxeqqou");
        connectionFactory.setPassword("z-3ZVFLkp9m_3yaeEZCxKH0KkthMRDoY");
        connectionFactory.setVirtualHost("umxeqqou");

        return connectionFactory;
    }


}
