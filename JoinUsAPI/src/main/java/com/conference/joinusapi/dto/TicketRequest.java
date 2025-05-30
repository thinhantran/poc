package com.conference.joinusapi.dto;

import lombok.Data;

@Data
public class TicketRequest {
    private Long id;
    private Long userId;
    private Long eventId;
    private int quantity;
}

