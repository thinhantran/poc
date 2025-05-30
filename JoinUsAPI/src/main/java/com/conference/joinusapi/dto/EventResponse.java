package com.conference.joinusapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String name;
    private String type;
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;
    private String description;
    private BigDecimal price;
    private String imageUrl;
}
