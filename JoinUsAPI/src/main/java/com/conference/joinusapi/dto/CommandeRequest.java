package com.conference.joinusapi.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommandeRequest {
    private Long id;
    private boolean registeredUser;

    private Long userId;

    private String fullName;
    private String email;
    private String phone;

    private List<Long> ticketIds;
}
