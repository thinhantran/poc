package com.conference.joinusapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketResponse {
    private Long id;
    private String eventTitle;
    private String date;
    private String time;
    private int quantity;
    private double unitPrice;
    private double total;
    private String image;
    private boolean isOrder;
}
