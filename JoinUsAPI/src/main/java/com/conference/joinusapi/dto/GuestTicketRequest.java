package com.conference.joinusapi.dto;

import lombok.Data;
import java.util.List;

@Data
public class GuestTicketRequest {
    private List<TicketItem> tickets;

    @Data
    public static class TicketItem {
        private String eventTitle;
        private String date;
        private String time;
        private int quantity;
    }
}
