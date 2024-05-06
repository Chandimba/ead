package com.ead.notification.dtos;

import lombok.Data;

@Data
public class NotificationCommandDTO {

    private String title;
    private String message;
    private String userId;

}
