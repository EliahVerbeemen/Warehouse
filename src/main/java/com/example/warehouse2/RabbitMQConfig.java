package com.example.warehouse2;


import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("sparrow.rmq.cloudamqp.com");
        connectionFactory.setUsername("umxeqqou");
        connectionFactory.setPassword("z-3ZVFLkp9m_3yaeEZCxKH0KkthMRDoY");
        connectionFactory.setVirtualHost("umxeqqou");

        return connectionFactory;
    }

    @Value("${queue.outgoing}")
    private String outgoingQueueName;
    @Value("${queue.incomming}")
    private String incommingQueueName;
    @Value("${exchange.exchangName}")
    private String exchangeName;
   @Value("${routingkey.incomingOrders}")
   private String routingKeyIncomingOrders;
    @Value("${routingkey.outgoingOrders}")
    private String routingKeyOutgoingOrders;

    //Kies de juiste import voor de queue
   @Bean
    public Queue queue(){

        return new Queue(outgoingQueueName,true);


    }
   /* @Bean
    public Queue secondQueue(){

        return new Queue(incommingQueueName,true);


    }*/
    //Berichten krijgen sleutel om bestemming te weten
    ///Voor de bakkery fanout==> Meerdere bestemmingen
    @Bean

    public TopicExchange topicExchange(){

        return new TopicExchange(exchangeName);


    }


    @Bean

    public Binding binding(){
    return    BindingBuilder.bind(queue()).to(topicExchange()).with(routingKeyIncomingOrders);


    }



/*@Bean
    public Binding secondBinding(){
        return    BindingBuilder.bind(secondQueue()).to(topicExchange()).with(routingKeyOutgoingOrders);


    }*/

    @Bean
RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate= new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {

        return new Jackson2JsonMessageConverter();
    }

}
