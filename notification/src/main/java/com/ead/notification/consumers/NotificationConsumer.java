package com.ead.notification.consumers;

import com.ead.notification.dtos.NotificationCommandDTO;
import com.ead.notification.enums.NotificationStatus;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.services.NotificationService;
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

    private final NotificationService notificationService;

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
        var notificationModel = new NotificationModel();
        BeanUtils.copyProperties(notificationCommandDTO, notificationModel);
        notificationModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        notificationModel.setStatus(NotificationStatus.CREATED);

        notificationService.saveNotification(notificationModel);
    }

}
