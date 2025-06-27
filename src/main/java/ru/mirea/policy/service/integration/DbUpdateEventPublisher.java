package ru.mirea.policy.service.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.mirea.policy.service.dto.ProductEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbUpdateEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    public void sendUpdateEvent(ProductEvent event) {
        log.info("Sending product update event: {}", event);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}