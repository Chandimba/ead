package com.ead.course.publishers;

import com.ead.course.dtos.NotificationCommandDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationCommandPublisher {

    private RabbitTemplate rabbitTemplate;

    @Value("${ead.broker.exchange.notificationCommandExhange}")
    private String notificationCommandExchange;

    @Value("${ead.broker.key.notificationCommandKey}")
    private String notificationCommandKey;

    public void publishNotificationCommand(NotificationCommandDTO notificationCommandDTO) {
        rabbitTemplate.convertAndSend(notificationCommandExchange, notificationCommandKey, notificationCommandDTO);
    }

}
