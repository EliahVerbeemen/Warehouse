package kdg.be;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

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

    @Bean
    public org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("sparrow.rmq.cloudamqp.com");
        connectionFactory.setUsername("umxeqqou");
        connectionFactory.setPassword("z-3ZVFLkp9m_3yaeEZCxKH0KkthMRDoY");
        connectionFactory.setVirtualHost("umxeqqou");

        return connectionFactory;
    }
    @Bean
    public Queue queue() {
        return new Queue(outgoingQueueName, true);
    }
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange()).with(routingKeyIncomingOrders);
    }

    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @Qualifier("ingAvailableQueue")
    public Queue IngredientsAvalableQueue() {
        return new Queue("IngredientsAvailableQueue", true);
    }

    @Bean
    @Qualifier("ingAvailableExchange")
    public DirectExchange IngredientsAvalableExchange() {
        return new DirectExchange("ingAvailableExchange");
    }

    @Bean
    @Qualifier("ingAvailableBinding")
    public Binding IngredientsAvalableBinding(@Qualifier("ingAvailableExchange") DirectExchange IngredientsAvalableExchange, @Qualifier("ingAvailableQueue") Queue IngredientsAvalableQueue) {
        return BindingBuilder.bind(IngredientsAvalableQueue).to(IngredientsAvalableExchange).with("");
    }
}
