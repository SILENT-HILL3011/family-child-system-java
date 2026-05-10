package com.parent.service.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitDelayConfig {

    // 死信交换机（用于延迟）
    public static final String DLX_EXCHANGE = "dlx.schedule.exchange";
    // 死信队列
    public static final String DLX_QUEUE = "dlx.schedule.queue";
    // 业务交换机
    public static final String BUSINESS_EXCHANGE = "business.schedule.exchange";
    // 业务队列（真正消费）
    public static final String BUSINESS_QUEUE = "business.schedule.queue";

    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(DLX_QUEUE)
                .withArgument("x-dead-letter-exchange", BUSINESS_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "schedule.key")
                .build();
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with("schedule.key");
    }

    @Bean
    public Queue businessQueue() {
        return new Queue(BUSINESS_QUEUE);
    }

    @Bean
    public DirectExchange businessExchange() {
        return new DirectExchange(BUSINESS_EXCHANGE);
    }

    @Bean
    public Binding businessBinding() {
        return BindingBuilder.bind(businessQueue()).to(businessExchange()).with("schedule.key");
    }
}
