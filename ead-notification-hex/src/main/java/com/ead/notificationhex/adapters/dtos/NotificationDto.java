package com.ead.notificationhex.adapters.dtos;

import com.ead.notificationhex.core.domain.enums.NotificationStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NotificationDto {

    @NotNull
    private NotificationStatus notificationStatus;

}
