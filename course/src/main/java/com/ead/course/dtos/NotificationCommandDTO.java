package com.ead.course.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class NotificationCommandDTO {

    private String title;
    private String message;
    private UUID userId;

}
