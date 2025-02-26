package net.darkness.ozonparser.amqp;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Getter
@Configuration
public class OcrAmqpConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.exchange.multiple}")
    private String exchangeMultiple;

    @Value("${rabbitmq.provider.queue.single}")
    private String queueProvider;

    @Value("${rabbitmq.consumer.queue.single}")
    private String queueConsumer;

    @Value("${rabbitmq.provider.queue.multiple}")
    private String queueProviderMultiple;

    @Value("${rabbitmq.consumer.queue.multiple}")
    private String queueConsumerMultiple;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Value("${rabbitmq.routing-key.multiple}")
    private String routingKeyMultiple;

    @Value("${rabbitmq.routing-key.ozon}")
    private String routingKeyOzon;

    @Value("${rabbitmq.routing-key.ozon.multiple}")
    private String routingKeyOzonMultiple;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public TopicExchange topicExchangeMultiple() {
        return new TopicExchange(exchangeMultiple);
    }

    @Bean
    public Queue queueConsumer() {
        return new Queue(queueConsumer, true, false, true);
    }

    @Bean
    public Queue queueProvider() {
        return new Queue(queueProvider, true, false, true);
    }

    @Bean
    public Queue queueConsumerMultiple() {
        return new Queue(queueConsumerMultiple, true, false, true);
    }

    @Bean
    public Queue queueProviderMultiple() {
        return new Queue(queueProviderMultiple, true, false, true);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueProvider())
                .to(topicExchange())
                .with(routingKeyOzon);
    }

    @Bean
    public Binding bindingMultiple() {
        return BindingBuilder.bind(queueProviderMultiple())
                .to(topicExchange())
                .with(routingKeyOzonMultiple);
    }

    @Bean
    public Binding bindingOzon() {
        return BindingBuilder.bind(queueConsumer())
                .to(topicExchange())
                .with(routingKey);
    }

    @Bean
    public Binding bindingOzonMultiple() {
        return BindingBuilder.bind(queueConsumerMultiple())
                .to(topicExchange())
                .with(routingKeyMultiple);
    }
}
