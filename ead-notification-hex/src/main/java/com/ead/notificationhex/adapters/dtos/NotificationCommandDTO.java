package com.ead.notificationhex.adapters.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationCommandDTO {

    private String title;
    private String message;
    private UUID userId;

}
