package com.nepxion.discovery.guide.service.middleware;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ankeway
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQOperation {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQOperation.class);

    private static final String EXCHANGE = "MyGroup";
    private static final String ROUTINGKEY = "MyDataId";

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Bean
    public Queue queue() {
        return new Queue(RabbitMQOperation.ROUTINGKEY);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(RabbitMQOperation.EXCHANGE);
    }

    @Bean
    Binding bindingExchangeMessages(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitMQOperation.ROUTINGKEY);
    }

    public void invokeRabbitMQ() {
        produceRabbitMQ();
    }

    public void produceRabbitMQ() {
        String message = "MyMessage";

        amqpTemplate.convertAndSend(RabbitMQOperation.EXCHANGE, RabbitMQOperation.ROUTINGKEY, message);

        LOG.info("RabbitMQ produce, exchange={}, routingKey={}, message={}", RabbitMQOperation.EXCHANGE, RabbitMQOperation.ROUTINGKEY, message);
    }

    @RabbitListener(queues = RabbitMQOperation.ROUTINGKEY)
    public void subscribeRabbitMQ(String message) {
        LOG.info("RabbitMQ subscribe, result={}", message);
    }
}