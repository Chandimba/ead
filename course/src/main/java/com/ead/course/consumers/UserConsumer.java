package com.ead.course.consumers;

import com.ead.course.dtos.UserEventDTO;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserConsumer {

    private final UserService userService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ead.broker.queue.userEventQueue}", durable = "true"),
            exchange = @Exchange(value = "${ead.broker.exchange.userEvent}", type = ExchangeTypes.FANOUT, ignoreDeclarationExceptions = "true")
    ))
    public void listenerUserEvent(@Payload UserEventDTO userEventDTO) {
        UserModel userModel = userEventDTO.convertToUserModel();

        switch (userEventDTO.getActionType()) {
            case "CREATE":
            case "UPDATE":
                userService.save(userModel);
                break;
            case "DELETE":
                userService.delete(userEventDTO.getUserId());
                break;
        }
    }

}
