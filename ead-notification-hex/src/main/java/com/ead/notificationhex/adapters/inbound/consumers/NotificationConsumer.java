package com.ead.notificationhex.adapters.inbound.consumers;

import com.ead.notificationhex.adapters.dtos.NotificationCommandDTO;
import com.ead.notificationhex.core.domain.NotificationDomain;
import com.ead.notificationhex.core.domain.enums.NotificationStatus;
import com.ead.notificationhex.core.ports.NotificationServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
@Component
public class NotificationConsumer {

    private final NotificationServicePort notificationService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${ead.queue.notificationCommandQueue}", durable = "true"),
                    exchange = @Exchange(
                            value = "${ead.broker.notificationCommandExhange}",
                            type = ExchangeTypes.TOPIC,
                            ignoreDeclarationExceptions = "true"
                    ),
                    key = "${ead.key.notificationCommandKey}"
            )
    )
    public void listen(@Payload NotificationCommandDTO notificationCommandDTO) {
        var notificationDomain = new NotificationDomain();
        BeanUtils.copyProperties(notificationCommandDTO, notificationDomain);
        notificationDomain.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        notificationDomain.setNotificationStatus(NotificationStatus.CREATED);

        notificationService.saveNotification(notificationDomain);
    }

}
