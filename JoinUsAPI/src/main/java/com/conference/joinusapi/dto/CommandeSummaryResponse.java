package com.conference.joinusapi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CommandeSummaryResponse {
    private Long commandeId;
    private String createdAt;
    private List<TicketResponse> tickets;
}
